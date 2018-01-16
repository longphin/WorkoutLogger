package com.longlife.workoutlogger.view;

import com.longlife.workoutlogger.model.ListItem;

import java.util.List;

/**
 * Created by Longphi on 12/31/2017.
 */

public interface ViewInterface {

    void startDetailActivity(String dateAndTime, String message, int colorResource);

    void setUpAdapterAndView(List<ListItem> listOfData);

}
