package com.hacktyki.car.AdminPanel;

public class AdminPanelPresenter {
    private final AdminPanelContract.View view;

    public AdminPanelPresenter(AdminPanelContract.View view) {
        this.view = view;
    }

    public void loadData(){
        view.loadData();
    }
}
