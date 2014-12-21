// Generated code from Butter Knife. Do not modify!
package com.aaaliua.user;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HtmlView$$ViewInjector {
  public static void inject(Finder finder, final com.aaaliua.user.HtmlView target, Object source) {
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
    view = finder.findRequiredView(source, 2131034224, "field 'tipsWebView'");
    target.tipsWebView = (android.webkit.WebView) view;
  }

  public static void reset(com.aaaliua.user.HtmlView target) {
    target.back = null;
    target.tipsWebView = null;
  }
}
