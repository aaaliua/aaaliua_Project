// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ConfirmPasswordActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.ConfirmPasswordActivity target, Object source) {
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
    view = finder.findRequiredView(source, 2131296342, "field 'next' and method 'onRegClick'");
    target.next = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onRegClick(p0);
        }
      });
  }

  public static void reset(com.aaaliua.user.ConfirmPasswordActivity target) {
    target.back = null;
    target.next = null;
  }
}