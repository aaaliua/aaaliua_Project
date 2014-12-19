// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.LoginActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296355, "field 'register' and method 'onRegClick'");
    target.register = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onRegClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131296356, "field 'login' and method 'onloginClick'");
    target.login = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onloginClick(p0);
        }
      });
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

  public static void reset(com.aaaliua.user.LoginActivity target) {
    target.register = null;
    target.login = null;
    target.back = null;
  }
}
