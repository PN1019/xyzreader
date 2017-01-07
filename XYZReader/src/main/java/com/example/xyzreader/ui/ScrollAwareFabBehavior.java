package com.example.xyzreader.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by abhi on 1/4/2017.
 */

public class ScrollAwareFabBehavior extends FloatingActionButton.Behavior
{
    public ScrollAwareFabBehavior(Context context, AttributeSet attributeSet){
        super();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View directTargetChild, View target, int nestedScrollAxes){
//check for vertical scrolling
        return nestedScrollAxes== ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout,floatingActionButton,directTargetChild,target,nestedScrollAxes);

    }


    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton floatingActionButton, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout,floatingActionButton,target,dxConsumed,dyConsumed,dxUnconsumed,dyUnconsumed);
        if (dyConsumed>0&&floatingActionButton.getVisibility()==View.VISIBLE)//scrolling down
        floatingActionButton.hide();
        else if (dyConsumed<0&&floatingActionButton.getVisibility()!=View.VISIBLE)//scrolling up{
        floatingActionButton.show();
    }
}