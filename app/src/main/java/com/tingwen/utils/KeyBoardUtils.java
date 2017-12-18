package com.tingwen.utils;


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.tingwen.app.GlobalContext;


/**
 * 描述: 键盘管理工具
 * 名称: KeyBoardUtils
 */
public class KeyBoardUtils {
	/**
	 * 打开软键盘
	 *
	 * @param mEditText 输入框
	 */
	public static void openKeyboard(EditText mEditText) {
		InputMethodManager imm = (InputMethodManager) GlobalContext.getInstance()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 *
	 * @param mEditText 输入框
	 */
	public static void closeKeyboard(View mEditText) {
		InputMethodManager imm = (InputMethodManager) GlobalContext.getInstance()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(), 0);
	}
}
