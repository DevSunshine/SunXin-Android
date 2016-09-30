package com.sunshine.sunxin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunshine.sunxin.BaseFragment;
import com.sunshine.sunxin.R;
import com.sunshine.sunxin.beans.contacts.Contacts;
import com.sunshine.sunxin.ui.business.contacs.ContactsAdapter;
import com.sunshine.sunxin.view.TitleView;

import java.util.ArrayList;
import java.util.List;


// Copyright (c) 2016 ${ORGANIZATION_NAME}. All rights reserved.

/**
 * Created by 钟光燕 on 2016/9/29.
 * e-mail guangyanzhong@163.com
 * 首页中，联系人页面
 */

public class TabContactsFragment extends BaseFragment {
    public static TabContactsFragment newInstance(){
        TabContactsFragment fragment = new TabContactsFragment() ;
        return fragment ;
    }

    private TitleView mTitle ;

    private RecyclerView mContactsList ;
    /**
     * 通讯录adapter
     */
    private ContactsAdapter mContactsAdapter ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_contacts_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TitleView) view.findViewById(R.id.id_tab_contacts_title);
        initTitle() ;
        initContactsList() ;
    }

    /**
     * 初始化通讯录列表
     */
    private void initContactsList() {
        mContactsList = (RecyclerView) getView().findViewById(R.id.id_contacts_list);
        mContactsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContactsAdapter = new ContactsAdapter() ;
        mContactsList.setAdapter(mContactsAdapter);

        /**
         * tests
         */

        addTestData() ;
    }

    private void addTestData(){
        List<Contacts> list = new ArrayList<>() ;
        Contacts contacts = new Contacts() ;
        contacts.name = "刘德华" ;
        contacts.nameOrder = "L" ;
        list.add(contacts) ;

        for (int i = 0 ; i < 10 ; i ++){
            Contacts contactt = new Contacts() ;
            contactt.name = "张学友 "+ i ;
            contactt.nameOrder = "Z" ;
            list.add(contactt) ;
        }

        for (int i = 0 ; i < 2 ; i ++){
            Contacts contactt = new Contacts() ;
            contactt.name = "钟光燕 "+ i ;
            contactt.nameOrder = "Z" ;
            list.add(contactt) ;
        }

        for (int i = 0 ; i < 5 ; i ++){
            Contacts contactt = new Contacts() ;
            contactt.name = "杨子荣 "+ i ;
            contactt.nameOrder = "Y" ;
            list.add(contactt) ;
        }
        mContactsAdapter.setContactsList(list);
    }

    private void initTitle() {
        mTitle.setTitle("联系人") ;
    }
    @Override
    public void fragmentSelect() {

    }
}
