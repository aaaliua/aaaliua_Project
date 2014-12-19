// Generated code from Butter Knife. Do not modify!
package com.aaaliua.itemwork;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.itemwork.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296338, "field 'add' and method 'tologin'");
    target.add = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.tologin(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296340, "field 'mSlidingTabLayout'");
    target.mSlidingTabLayout = (com.dazhongcun.widget.PagerSlidingTabStrip) view;
    view = finder.findRequiredView(source, 2131296337, "field 'userImage' and method 'toUser'");
    target.userImage = (com.dazhongcun.widget.CircleImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.toUser(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296341, "field 'mViewPager'");
    target.mViewPager = (android.support.v4.view.ViewPager) view;
    view = finder.findRequiredView(source, 2131296336, "field 'tab'");
    target.tab = (android.widget.RelativeLayout) view;
  }

  public static void reset(com.aaaliua.itemwork.MainActivity target) {
    target.add = null;
    target.mSlidingTabLayout = null;
    target.userImage = null;
    target.mViewPager = null;
    target.tab = null;
  }
}
