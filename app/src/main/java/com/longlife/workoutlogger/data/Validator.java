/*
 * Created by Longphi Nguyen on 12/11/18 8:25 PM.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/18 9:17 PM.
 */

package com.longlife.workoutlogger.data;

import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.Required;
import com.longlife.workoutlogger.CustomAnnotationsAndExceptions.RequiredFieldException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

// This class will validate an object, checking for the @Required annotation for required fields
// Based on validator by Ishan Khanna - https://www.codementor.io/ishan1604/validating-models-user-inputs-java-android-du107w0st
public class Validator {

    public static boolean validateForNulls(Object objectToValidate)
            throws RequiredFieldException, ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //Get all the fields of the class
        Field[] declaredFields = objectToValidate.getClass().getDeclaredFields();

        /*
         *  Iterate over each field to check if that field
         *  has the "Required" annotation declared for it or not
         */
        for (Field field : declaredFields) {

            Annotation annotation = field.getAnnotation(Required.class);

            /*
             *  Check if the annotation is present on that field
             */
            if (annotation != null) {

                Required required = (Required) annotation;

                /*
                 *  Check if it says this field is required
                 */
                if (required.value()) {
                    /*
                     *  Now we make sure we can access the private
                     *  fields also, so we need to call this method also
                     *  other wise we would get a {@link java.lang.IllegalAccessException}
                     */
                    field.setAccessible(true);
                    /*
                     *  If this field is required, then it should be present
                     *  in the declared fields array, if it is throw the
                     *  {@link RequiredFieldException}
                     */
                    if (field.get(objectToValidate) == null
                            || (field.getType().isInstance("string") && field.get(objectToValidate).toString().trim().isEmpty()) // If the field is a String, then check if it is empty.
                    ) {
                        throw new RequiredFieldException(objectToValidate.getClass().getName() + "." + field.getName());
                    }
                }
            }
        }
        return true;
    }

}

