/*
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute this software, either
 * in source code form or as a compiled binary, for any purpose, commercial or non-commercial, and
 * by any means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors of this software dedicate
 * any and all copyright interest in the software to the public domain. We make this dedication for
 * the benefit of the public at large and to the detriment of our heirs and successors. We intend
 * this dedication to be an overt act of relinquishment in perpetuity of all present and future
 * rights to this software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */

package com.github.pffy.hexrgbplus;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * HexRgbPlusActivity - HEXRGB+ = HEXRGB plus some features.
 * 
 * @license http://unlicense.org/ The Unlicense
 * @version 2.1 (r1)
 * @link https://github.com/pffy/
 * @author The Pffy Authors
 */

public class HexRgbPlusActivity extends Activity {

  private final String ASSET_CLRTXT = "IdxHexColorNames.txt";

  private int colorRed = 0;
  private int colorGreen = 0;
  private int colorBlue = 0;

  private boolean grayMode = false;

  private Properties colorNames;

  private String saveHex1 = "";
  private String saveHex2 = "";
  private String saveHex3 = "";
  private String saveHex4 = "";
  private String saveHex5 = "";

  private LinearLayout backdrop;

  private TextView tv_hashtag;
  private TextView tv_colorname;

  private Button btn_set;
  private Button btn_black;
  private Button btn_white;

  private Button btn_savebox1;
  private Button btn_savebox2;
  private Button btn_savebox3;
  private Button btn_savebox4;
  private Button btn_savebox5;

  private EditText et_r;
  private EditText et_g;
  private EditText et_b;
  private EditText et_hex;

  private SeekBar sb_r;
  private SeekBar sb_g;
  private SeekBar sb_b;

  private ToggleButton tgb;


  // loads app UI, sets default preferences
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hex_rgb_plus);

    // GETLAYOUT: Views you can use
    this.backdrop = (LinearLayout) findViewById(R.id.linlay_background);

    this.tv_hashtag = (TextView) findViewById(R.id.tv_hashtag);
    this.tv_colorname = (TextView) findViewById(R.id.tv_x11colorname);

    this.et_r = (EditText) findViewById(R.id.et_red);
    this.et_g = (EditText) findViewById(R.id.et_green);
    this.et_b = (EditText) findViewById(R.id.et_blue);
    this.et_hex = (EditText) findViewById(R.id.et_hextext);

    this.sb_r = (SeekBar) findViewById(R.id.sb_red);
    this.sb_g = (SeekBar) findViewById(R.id.sb_green);
    this.sb_b = (SeekBar) findViewById(R.id.sb_blue);

    this.btn_set = (Button) findViewById(R.id.btn_set);
    this.btn_black = (Button) findViewById(R.id.btn_black);
    this.btn_white = (Button) findViewById(R.id.btn_white);

    this.btn_savebox1 = (Button) findViewById(R.id.btn_savebox1);
    this.btn_savebox2 = (Button) findViewById(R.id.btn_savebox2);
    this.btn_savebox3 = (Button) findViewById(R.id.btn_savebox3);
    this.btn_savebox4 = (Button) findViewById(R.id.btn_savebox4);
    this.btn_savebox5 = (Button) findViewById(R.id.btn_savebox5);

    this.tgb = (ToggleButton) findViewById(R.id.tgbtn_graymode);

    // GETDATA: load properties
    this.colorNames = new Properties();

    try {
      this.colorNames.load(getResources().getAssets().open(this.ASSET_CLRTXT));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // GETPREFS: get persistent preferences
    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);

    // get stored RGB values
    this.colorRed = shpref.getInt(getString(R.string.prefkey_int_color_red), 0);
    this.colorGreen = shpref.getInt(getString(R.string.prefkey_int_color_green), 0);
    this.colorBlue = shpref.getInt(getString(R.string.prefkey_int_color_blue), 0);

    // get stored gray mode status
    this.grayMode = shpref.getBoolean(getString(R.string.prefkey_bool_gray_mode), false);

    // get stored color values
    this.saveHex1 = shpref.getString(getString(R.string.prefkey_str_savehex_one), "");
    this.saveHex2 = shpref.getString(getString(R.string.prefkey_str_savehex_two), "");
    this.saveHex3 = shpref.getString(getString(R.string.prefkey_str_savehex_three), "");
    this.saveHex4 = shpref.getString(getString(R.string.prefkey_str_savehex_four), "");
    this.saveHex5 = shpref.getString(getString(R.string.prefkey_str_savehex_five), "");

    // OBSERVE: Event listeners
    this.et_hex.setOnFocusChangeListener(this.focusHandler);
    this.et_hex.setOnKeyListener(this.keyhandler);

    this.et_r.setOnFocusChangeListener(this.focusHandler);
    this.et_g.setOnFocusChangeListener(this.focusHandler);
    this.et_b.setOnFocusChangeListener(this.focusHandler);

    this.et_r.setOnEditorActionListener(this.editHandler);
    this.et_g.setOnEditorActionListener(this.editHandler);
    this.et_b.setOnEditorActionListener(this.editHandler);

    this.et_hex.setOnEditorActionListener(this.editHandler);

    this.sb_r.setOnSeekBarChangeListener(this.seekHandler);
    this.sb_g.setOnSeekBarChangeListener(this.seekHandler);
    this.sb_b.setOnSeekBarChangeListener(this.seekHandler);

    this.btn_set.setOnClickListener(this.clickHandler);
    this.btn_black.setOnClickListener(this.clickHandler);
    this.btn_white.setOnClickListener(this.clickHandler);

    this.btn_savebox1.setOnClickListener(clickHandler);
    this.btn_savebox2.setOnClickListener(clickHandler);
    this.btn_savebox3.setOnClickListener(clickHandler);
    this.btn_savebox4.setOnClickListener(clickHandler);
    this.btn_savebox5.setOnClickListener(clickHandler);

    this.btn_savebox1.setOnLongClickListener(pokeHandler);
    this.btn_savebox2.setOnLongClickListener(pokeHandler);
    this.btn_savebox3.setOnLongClickListener(pokeHandler);
    this.btn_savebox4.setOnLongClickListener(pokeHandler);
    this.btn_savebox5.setOnLongClickListener(pokeHandler);
    
    this.tgb.setOnCheckedChangeListener(this.checkHandler);

    // UPDATE: updates layout based on properties
    this.updateByRgb();
  }

  // SETPREFS: saves persistently, just before app quits
  @Override
  protected void onDestroy() {

    super.onDestroy();

    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = shpref.edit();

    // store RGB values
    editor.putInt(getString(R.string.prefkey_int_color_red), this.colorRed);
    editor.putInt(getString(R.string.prefkey_int_color_green), this.colorGreen);
    editor.putInt(getString(R.string.prefkey_int_color_blue), this.colorBlue);

    // store gray mode status
    editor.putBoolean(getString(R.string.prefkey_bool_gray_mode), this.grayMode);

    // store saved color values
    editor.putString(getString(R.string.prefkey_str_savehex_one), this.saveHex1);
    editor.putString(getString(R.string.prefkey_str_savehex_two), this.saveHex2);
    editor.putString(getString(R.string.prefkey_str_savehex_three), this.saveHex3);
    editor.putString(getString(R.string.prefkey_str_savehex_four), this.saveHex4);
    editor.putString(getString(R.string.prefkey_str_savehex_five), this.saveHex5);

    editor.commit();
  }

  // SAVESTATE: Saves the app instance state
  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {

    super.onSaveInstanceState(savedInstanceState);

    // save RGB colors
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_red), this.colorRed);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_green), this.colorGreen);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_blue), this.colorBlue);

    // save gray mode status
    savedInstanceState.putBoolean(getString(R.string.prefkey_bool_gray_mode), this.grayMode);

    // save saved color values
    savedInstanceState.putString(getString(R.string.prefkey_str_savehex_one), this.saveHex1);
    savedInstanceState.putString(getString(R.string.prefkey_str_savehex_two), this.saveHex2);
    savedInstanceState.putString(getString(R.string.prefkey_str_savehex_three), this.saveHex3);
    savedInstanceState.putString(getString(R.string.prefkey_str_savehex_four), this.saveHex4);
    savedInstanceState.putString(getString(R.string.prefkey_str_savehex_five), this.saveHex5);
  }

  // GETSTATE: Gets the app instance state
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    // get RGB colors
    this.colorRed = savedInstanceState.getInt(getString(R.string.prefkey_int_color_red));
    this.colorGreen = savedInstanceState.getInt(getString(R.string.prefkey_int_color_green));
    this.colorBlue = savedInstanceState.getInt(getString(R.string.prefkey_int_color_blue));

    // get gray mode status
    this.grayMode = savedInstanceState.getBoolean(getString(R.string.prefkey_bool_gray_mode));

    // get saved color values
    this.saveHex1 = savedInstanceState.getString(getString(R.string.prefkey_str_savehex_one));
    this.saveHex2 = savedInstanceState.getString(getString(R.string.prefkey_str_savehex_two));
    this.saveHex3 = savedInstanceState.getString(getString(R.string.prefkey_str_savehex_three));
    this.saveHex4 = savedInstanceState.getString(getString(R.string.prefkey_str_savehex_four));
    this.saveHex5 = savedInstanceState.getString(getString(R.string.prefkey_str_savehex_five));

    this.updateByRgb();
  }

  // updates color based on hexadecimal input
  private void updateByHex() {

    String hexString;
    int[] rgb = new int[3];

    hexString = this.et_hex.getText().toString();
    rgb = hex2rgb(hexString);

    this.colorRed = rgb[0];
    this.colorGreen = rgb[1];
    this.colorBlue = rgb[2];

    this.grayMode = false;
    this.tgb.setChecked(false);

    this.updateByRgb();
  }

  // updates color based on RGB input
  private void updateByRgb() {

    int r, g, b;

    r = this.colorRed;
    this.sb_r.setProgress(r);
    this.et_r.setText(r + "");

    if (this.grayMode) {

      this.colorGreen = this.colorBlue = r;

      this.sb_g.setProgress(r);
      this.sb_b.setProgress(r);

      this.et_g.setText(r + "");
      this.et_b.setText(r + "");

      this.et_hex.setText(this.rgb2hex(r, r, r));

    } else {

      g = this.colorGreen;
      b = this.colorBlue;

      this.sb_g.setProgress(g);
      this.sb_b.setProgress(b);

      this.et_g.setText(g + "");
      this.et_b.setText(b + "");

      this.et_hex.setText(this.rgb2hex(r, g, b));
    }

    this.updateBackground();
  }

  // updates background to match color, keeps hashtag legible
  private void updateBackground() {

    int r, g, b;
    int contrastColor;

    r = this.colorRed;
    g = this.colorGreen;
    b = this.colorBlue;

    contrastColor = this.getContrastColor(r, g, b);

    this.backdrop.setBackgroundColor(Color.rgb(r, g, b));
    this.tv_hashtag.setTextColor(contrastColor);
    this.tv_colorname.setTextColor(contrastColor);

    this.tv_colorname.setText(this.colorNames.getProperty(this.et_hex.getText().toString(),
        getString(R.string.str_blank)));
  }

  // determines whether hashtag should be black or white
  private int getContrastColor(int r, int g, int b) {

    double rx, gx, bx;
    rx = 0.213 * r / 255;
    gx = 0.715 * g / 255;
    bx = 0.072 * b / 255;

    if (rx + gx + bx < 0.5) {
      return Color.WHITE;
    } else {
      return Color.BLACK;
    }
  }

  // sets current color to black
  private void setToBlack() {

    this.colorRed = 0;
    this.colorGreen = 0;
    this.colorBlue = 0;
    this.updateByRgb();
  }

  // sets current color to white
  private void setToWhite() {

    this.colorRed = 255;
    this.colorGreen = 255;
    this.colorBlue = 255;
    this.updateByRgb();
  }

  // filters RGB color input value
  private int processRgbInput(String input) {

    int value = 0;

    try {

      value = Integer.parseInt(input);

      // value is bounded between 0 and 255
      value = Math.max(0, value);
      value = Math.min(value, 255);

    } catch (Exception ex) {
      value = 0; // no problem. moving along.
    }

    return value;
  }

  private String dec2hex(int value) {

    String hex;

    try {

      // dec to hex
      hex = Integer.toHexString(value);

      // single-digit to double-digit hex
      hex = "00".substring(hex.length()) + hex;

      // uppercase HEX
      hex = hex.toUpperCase(Locale.US);

    } catch (Exception e) {

      // unlikely Exception. anyways, reset to [00].
      hex = "00";
    }

    return hex;
  }

  // converts RGB color values into hexadecimal
  private String rgb2hex(int r, int g, int b) {
    return dec2hex(r) + "" + dec2hex(g) + "" + dec2hex(b);
  }

  // converts hexadecimal into RGB color values
  private int[] hex2rgb(String hex) {

    int color;
    int[] rgb = new int[3];

    try {

      color = Color.parseColor("#" + hex);

      rgb[0] = Color.red(color);
      rgb[1] = Color.green(color);
      rgb[2] = Color.blue(color);

    } catch (Exception ex) {
      // no problem. just move on.
      rgb[0] = rgb[1] = rgb[2] = 0;
    }

    return rgb;
  }

  /*
   * Event Listeners - Nice, clean way to respond to user wants and needs.
   */

  private View.OnKeyListener keyhandler = new View.OnKeyListener() {

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

      switch (keyCode) {
        case KeyEvent.KEYCODE_ENTER:
        case KeyEvent.KEYCODE_DPAD_CENTER:
          updateByHex();
          return true;
        default:
          // do nothing here.
          break;

      }

      return false;
    }
  };

  private TextView.OnEditorActionListener editHandler = new TextView.OnEditorActionListener() {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

      int viewId = 0;
      int value = 0;
      String hexString;
      EditText et;

      viewId = v.getId();
      et = (EditText) findViewById(viewId);
      hexString = et.getText().toString();
      value = processRgbInput(hexString);

      try {

        switch (actionId) {
          case EditorInfo.IME_ACTION_NEXT:
          case EditorInfo.IME_ACTION_DONE:
          case EditorInfo.IME_ACTION_GO:

            switch (viewId) {
              case R.id.et_red:
                colorRed = value;
                break;
              case R.id.et_green:
                colorGreen = value;
                break;
              case R.id.et_blue:
                colorBlue = value;
                break;
              case R.id.et_hextext:
                updateByHex();
                break;
              default:
                // do nothing
                break;
            }

            updateByRgb();

            break;
          default:
            // do nothing
            break;
        }

        return true;

      } catch (Exception ex) {
        // do nothing. relax.
      }

      return false;
    }
  };

  private CompoundButton.OnCheckedChangeListener checkHandler =
      new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton bv, boolean isChecked) {

          // gray mode only needs red SeekBar enabled
          if (isChecked) {

            sb_g.setEnabled(false);
            sb_b.setEnabled(false);

            et_g.setEnabled(false);
            et_b.setEnabled(false);

            grayMode = true;

            Toast.makeText(bv.getContext(), getResources().getString(R.string.str_graymode_tip),
                Toast.LENGTH_LONG).show();

          } else {

            sb_g.setEnabled(true);
            sb_b.setEnabled(true);

            et_g.setEnabled(true);
            et_b.setEnabled(true);

            grayMode = false;
          }

          updateByRgb();
        }
      };

  private View.OnFocusChangeListener focusHandler = new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

      if (!hasFocus) {

        int value = 0;
        EditText et = (EditText) findViewById(v.getId());

        value = processRgbInput(et.getText().toString());

        // process input text
        et.setText(value + "");

        switch (v.getId()) {
          case R.id.et_red:
            colorRed = value;
            break;
          case R.id.et_green:
            colorGreen = value;
            break;
          case R.id.et_blue:
            colorBlue = value;
            break;
          default:
            // do nothing.
            break;
        }

        updateByRgb();
      }
    }

  };


  private View.OnLongClickListener pokeHandler = new View.OnLongClickListener() {

    @Override
    public boolean onLongClick(View v) {

      int r, g, b;

      r = colorRed;
      g = colorGreen;
      b = colorBlue;

      String hex = rgb2hex(r, g, b);
      et_hex.setText(hex);

      int id = v.getId();

      // get the specific button 'poked'
      Button btn = (Button) findViewById(id);
      btn.setTextColor(getContrastColor(r, g, b));
      btn.setBackgroundColor(Color.rgb(r, g, b));

      switch (id) {
        case R.id.btn_savebox1:
          saveHex1 = hex;
          break;
        case R.id.btn_savebox2:
          saveHex2 = hex;
          break;
        case R.id.btn_savebox3:
          saveHex3 = hex;
          break;
        case R.id.btn_savebox4:
          saveHex4 = hex;
          break;
        case R.id.btn_savebox5:
          saveHex5 = hex;
          break;

        default:
          // do nothing
          return false;
      }

      updateByHex();
      Toast.makeText(getBaseContext(), getString(R.string.str_savebox_tip) + "", Toast.LENGTH_LONG)
          .show();


      return true;
    }
  };

  private View.OnClickListener clickHandler = new View.OnClickListener() {

    @Override
    public void onClick(View v) {

      // NOTE: String.isEmpty() requires API 9, so String.equals() is used.

      switch (v.getId()) {

        case R.id.btn_savebox1:
          if (!saveHex1.equals("")) {
            et_hex.setText(saveHex1);
            updateByHex();
          }
          break;
        case R.id.btn_savebox2:
          if (!saveHex2.equals("")) {
            et_hex.setText(saveHex2);
            updateByHex();
          }
          break;
        case R.id.btn_savebox3:
          if (!saveHex3.equals("")) {
            et_hex.setText(saveHex3);
            updateByHex();
          }
          break;
        case R.id.btn_savebox4:
          if (!saveHex4.equals("")) {
            et_hex.setText(saveHex4);
            updateByHex();
          }
          break;
        case R.id.btn_savebox5:
          if (!saveHex5.equals("")) {
            et_hex.setText(saveHex5);
            updateByHex();
          }
          break;
        case R.id.btn_set:
          updateByHex();
          break;
        case R.id.btn_black:
          setToBlack();
          break;
        case R.id.btn_white:
          setToWhite();
          break;

        default:
          // do nothing
          break;
      }
    }

  };

  private SeekBar.OnSeekBarChangeListener seekHandler = new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      // nothing
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      // nothing
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

      if (fromUser) {
        switch (seekBar.getId()) {
          case R.id.sb_red:
            colorRed = progress;
            break;
          case R.id.sb_green:
            colorGreen = progress;
            break;
          case R.id.sb_blue:
            colorBlue = progress;
            break;
          default:
            // do nothing
            break;
        }

        updateByRgb();
      }
    }

  };
}
