package com.sunshine.sunxin.skin;

// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.sunshine.sunxin.skin.attr.AttrBackground;
import com.sunshine.sunxin.skin.attr.AttrImgSrc;
import com.sunshine.sunxin.skin.attr.AttrTextColor;
import com.sunshine.sunxin.skin.attr.SkinAttr;
import com.sunshine.sunxin.skin.skip.SkinPage;
import com.sunshine.sunxin.skin.skip.SkinView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.sunshine.sunxin.skin.attr.SkinAttr.BACKGROUND;
import static com.sunshine.sunxin.skin.attr.SkinAttr.SKIN_ATTR_NAME;
import static com.sunshine.sunxin.skin.attr.SkinAttr.SKIN_VALUE_FLAG;
import static com.sunshine.sunxin.skin.attr.SkinAttr.SRC;
import static com.sunshine.sunxin.skin.attr.SkinAttr.TEXT_COLOR;

/**
 * Created by 钟光燕 on 2016/10/13.
 * e-mail guangyanzhong@163.com
 */
class SkinFactory implements LayoutInflater.Factory {


    private List<SkinView> mSkinViewList;
    private String mActivityName;
    private static Method sCreateViewMethod;

    static final Class<?>[] mConstructorSignature = new Class[]{
            Context.class, AttributeSet.class};

    SkinFactory(String name) {
        mActivityName = name;
        SkinPage mSkinPage = new SkinPage();
        mSkinViewList = new ArrayList<>();
        mSkinPage.skins = mSkinViewList;
        SkinManager.getInstance().addSkinPage(mActivityName, mSkinPage);
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        Log.v("zgy", "=============name========" + name);
        View view = createView(name, context, attrs);
        collectSkinItem(view, context, attrs);
        return view;
    }

    private void collectSkinItem(View view, Context context, AttributeSet attrs) {
        if (view == null) {
            return;
        }
        String skinTag = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", SKIN_ATTR_NAME);
        if (TextUtils.equals(skinTag, SKIN_VALUE_FLAG)) {
            SkinView skinView = new SkinView();
            skinView.view = view;
            List<SkinAttr> skinAttrs = new ArrayList<>();
            skinView.attrs = skinAttrs;
            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attrName = attrs.getAttributeName(i);
                String attrValue = attrs.getAttributeValue(i);
                if (!isSupportedAttr(attrName)) {
                    continue;
                }
                if (attrValue.startsWith("@")) {
                    try {
                        int id = Integer.parseInt(attrValue.substring(1));
                        String entryName = context.getResources().getResourceEntryName(id);
                        String typeName = context.getResources().getResourceTypeName(id);
                        SkinAttr attr = null;
                        if (TextUtils.equals(attrName, SRC)) {
                            attr = new AttrImgSrc();
                        } else if (TextUtils.equals(attrName, BACKGROUND)) {
                            attr = new AttrBackground();
                        } else if (TextUtils.equals(attrName, TEXT_COLOR)) {
                            attr = new AttrTextColor();
                        }
                        if (attr == null) {
                            continue;
                        }
                        attr.attrName = attrName;
                        attr.attrType = typeName;
                        attr.attrValueName = entryName;
                        skinAttrs.add(attr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
            SkinManager.getInstance().applyView(skinView);
            mSkinViewList.add(skinView);
        }
    }

    private View createView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (-1 == name.indexOf('.')) {
            if ("View".equals(name)) {
                view = realCreateView(name, context, "android.view.", attrs);
            }
            if (view == null) {
                view = realCreateView(name, context, "android.widget.", attrs);
            }
            if (view == null) {
                view = realCreateView(name, context, "android.webkit.", attrs);
            }
        } else {
            view = realCreateView(name, context, null, attrs);
        }

        return view;
    }

    private View realCreateView(String name, Context context, String prefix, AttributeSet attrs) {
        View view = null;
        try {
            Class<? extends View> clazz;
            Constructor<? extends View> constructor;
            clazz = context.getClassLoader().loadClass(
                    prefix != null ? (prefix + name) : name).asSubclass(View.class);
            constructor = clazz.getConstructor(mConstructorSignature);
            constructor.setAccessible(true);
            Object[] args = new Object[2];
            args[0] = context ;
            args[1] = attrs;
            view = constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    private boolean isSupportedAttr(String attrName) {
        if (BACKGROUND.equals(attrName)) {
            return true;
        }
        if (TEXT_COLOR.equals(attrName)) {
            return true;
        }
        if (SRC.equals(attrName)) {
            return true;
        }
        return false;
    }

    public void clearPage() {
        SkinManager.getInstance().removeSkinPage(mActivityName);
    }
}
