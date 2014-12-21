// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SelectSchoolActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.SelectSchoolActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034146, "field 'back' and method 'onBackClick'");
    target.back = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onBackClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034222, "field 'query'");
    target.query = (android.widget.EditText) view;
    view = finder.findRequiredView(source, 2131034223, "field 'hint'");
    target.hint = (android.widget.TextView) view;
  }

  public static void reset(com.aaaliua.user.SelectSchoolActivity target) {
    target.back = null;
    target.query = null;
    target.hint = null;
  }
}
