// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.RegisterActivity target, Object source) {
    View view;
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
    view = finder.findRequiredView(source, 2131296359, "field 'nextPassword' and method 'onNextPasswordClick'");
    target.nextPassword = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onNextPasswordClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296358, "field 'validate_btn' and method 'onValidate'");
    target.validate_btn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onValidate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296357, "field 'validate_layout'");
    target.validate_layout = view;
  }

  public static void reset(com.aaaliua.user.RegisterActivity target) {
    target.back = null;
    target.nextPassword = null;
    target.validate_btn = null;
    target.validate_layout = null;
  }
}
