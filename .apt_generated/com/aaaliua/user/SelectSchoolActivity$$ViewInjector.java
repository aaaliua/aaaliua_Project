// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SelectSchoolActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.SelectSchoolActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296360, "field 'query'");
    target.query = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131296289, "field 'back' and method 'onBackClick'");
    target.back = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onBackClick(p0);
        }
      });
  }

  public static void reset(com.aaaliua.user.SelectSchoolActivity target) {
    target.query = null;
    target.back = null;
  }
}
