package com.longlife.workoutlogger;

import com.longlife.workoutlogger.model.DataSourceInterface;
import com.longlife.workoutlogger.model.ExerciseTemp;
import com.longlife.workoutlogger.controller.ExerciseController;
import com.longlife.workoutlogger.view.ViewInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ControllerUnitTest {
    /**
     * Test Double: specifically a "Mock"
     */
    @Mock
    DataSourceInterface dataSource;

    @Mock
    ViewInterface view;

    ExerciseController controller;

    private static final ExerciseTemp testItem = new ExerciseTemp(
            "a",
            "b",
            R.color.RED
    );

    @Before
    public void setUpTest()
    {
        MockitoAnnotations.initMocks(this);
        controller = new ExerciseController(view, dataSource);
    }

    @Test
    public void onGetListDataSuccessful() throws Exception {
        // set up any data we need
        ArrayList<ExerciseTemp> listOfData = new ArrayList<>();
        listOfData.add(testItem);

        // set up Mocks to return data that we want
        Mockito.when(dataSource.getListOfExercises())
                .thenReturn(listOfData);

        // call the method we are testing
        controller.getListFromDataSource();

        // check how the tested class responds to the data it receives or test its behavior
        Mockito.verify(view).setUpAdapterAndView(listOfData);
    }

    @Test
    public void onExerciseClicks()
    {
        controller.onExerciseClick(testItem);

        Mockito.verify(view).startDetailActivity(
                testItem.getDateAndTime(),
                testItem.getMessage(),
                testItem.getColorResource());
    }
}
