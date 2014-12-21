// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterActivity$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.RegisterActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131034219, "field 'selectXieyi'");
    target.selectXieyi = (android.widget.CheckBox) view;
    view = finder.findRequiredView(source, 2131034218, "field 'validate_btn' and method 'onValidate'");
    target.validate_btn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onValidate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034216, "field 'clientCode'");
    target.clientCode = (android.widget.EditText) view;
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
    view = finder.findRequiredView(source, 2131034221, "field 'nextPassword' and method 'onNextPasswordClick'");
    target.nextPassword = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onNextPasswordClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034217, "field 'validate_layout'");
    target.validate_layout = view;
    view = finder.findRequiredView(source, 2131034220, "field 'xieyiView' and method 'onXieyiClick'");
    target.xieyiView = view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onXieyiClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131034215, "field 'phone'");
    target.phone = (android.widget.EditText) view;
  }

  public static void reset(com.aaaliua.user.RegisterActivity target) {
    target.selectXieyi = null;
    target.validate_btn = null;
    target.clientCode = null;
    target.back = null;
    target.nextPassword = null;
    target.validate_layout = null;
    target.xieyiView = null;
    target.phone = null;
  }
}
