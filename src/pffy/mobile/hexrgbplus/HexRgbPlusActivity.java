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

package pffy.mobile.hexrgbplus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * HexRgbPlusActivity - HEXRGB+ (Experimental)
 * 
 * @license http://unlicense.org/ The Unlicense
 * @version 2.15 (r15)
 * @link https://github.com/pffy/
 * @author The Pffy Authors
 */

public class HexRgbPlusActivity extends Activity {

  private int mColorRed = 0;
  private int mColorGreen = 0;
  private int mColorBlue = 0;

  private boolean mParadeMode = false;

  private LinearLayout mLinearLayoutApp;

  private TextView mTextViewHashTag;
  private TextView mTextViewColorName;

  private Button mButtonSet;
  private Button mButtonBlack;
  private Button mButtonWhite;

  private EditText mEditTextRed;
  private EditText mEditTextGreen;
  private EditText mEditTextBlue;
  private EditText mEditTextHex;

  private SeekBar mSeekBarRed;
  private SeekBar mSeekBarGreen;
  private SeekBar mSeekBarBlue;

  private ToggleButton mToggleButtonParadeMode;

  private Button mButtonSaveBox1;
  private Button mButtonSaveBox2;
  private Button mButtonSaveBox3;
  private Button mButtonSaveBox4;
  private Button mButtonSaveBox5;

  private LinkedHashMap<String, String> mLinkedHashMapColorNames = new LinkedHashMap<String, String>();

  private int mDefaultColor = Color.WHITE;

  private int mColorPreset1 = this.mDefaultColor;
  private int mColorPreset2 = this.mDefaultColor;
  private int mColorPreset3 = this.mDefaultColor;
  private int mColorPreset4 = this.mDefaultColor;
  private int mColorPreset5 = this.mDefaultColor;

  private Spinner mSpinnerColorNames;

  // loads app UI, sets default preferences
  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hex_rgb_plus);

    // GETLAYOUT: Views you can use
    this.mLinearLayoutApp = (LinearLayout) findViewById(R.id.linearlayout_app);

    this.mTextViewHashTag = (TextView) findViewById(R.id.textview_hashtag);
    this.mTextViewColorName = (TextView) findViewById(R.id.textview_colorname);

    this.mEditTextRed = (EditText) findViewById(R.id.edittext_red);
    this.mEditTextGreen = (EditText) findViewById(R.id.edittext_green);
    this.mEditTextBlue = (EditText) findViewById(R.id.edittext_blue);
    this.mEditTextHex = (EditText) findViewById(R.id.edittext_hextext);

    this.mSeekBarRed = (SeekBar) findViewById(R.id.seekbar_red);
    this.mSeekBarGreen = (SeekBar) findViewById(R.id.seekbar_green);
    this.mSeekBarBlue = (SeekBar) findViewById(R.id.seekbar_blue);

    this.mButtonSet = (Button) findViewById(R.id.button_set);
    this.mButtonBlack = (Button) findViewById(R.id.button_black);
    this.mButtonWhite = (Button) findViewById(R.id.button_white);

    this.mButtonSaveBox1 = (Button) findViewById(R.id.button_savebox1);
    this.mButtonSaveBox2 = (Button) findViewById(R.id.button_savebox2);
    this.mButtonSaveBox3 = (Button) findViewById(R.id.button_savebox3);
    this.mButtonSaveBox4 = (Button) findViewById(R.id.button_savebox4);
    this.mButtonSaveBox5 = (Button) findViewById(R.id.button_savebox5);

    this.mToggleButtonParadeMode = (ToggleButton) findViewById(R.id.togglebutton_parade_mode);


    // GETDATA: load color names
    this.letsgo();

    // GETPREFS: get persistent preferences
    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);

    // get stored RGB values
    this.mColorRed = shpref.getInt(getString(R.string.prefkey_int_color_red), 0);
    this.mColorGreen = shpref.getInt(getString(R.string.prefkey_int_color_green), 0);
    this.mColorBlue = shpref.getInt(getString(R.string.prefkey_int_color_blue), 0);

    // get stored parade mode status
    this.mParadeMode = shpref.getBoolean(getString(R.string.prefkey_bool_parade_mode), false);

    this.mToggleButtonParadeMode.setChecked(this.mParadeMode);

    // get stored color preset values
    this.mColorPreset1 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_one), this.mDefaultColor);
    this.mColorPreset2 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_two), this.mDefaultColor);
    this.mColorPreset3 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_three), this.mDefaultColor);
    this.mColorPreset4 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_four), this.mDefaultColor);
    this.mColorPreset5 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_five), this.mDefaultColor);

    // OBSERVE: Event listeners
    this.mEditTextHex.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextHex.setOnKeyListener(this.mKeyHandler);

    this.mEditTextRed.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextGreen.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextBlue.setOnFocusChangeListener(this.mFocusHandler);

    this.mEditTextRed.setOnEditorActionListener(this.mEditHandler);
    this.mEditTextGreen.setOnEditorActionListener(this.mEditHandler);
    this.mEditTextBlue.setOnEditorActionListener(this.mEditHandler);

    this.mEditTextHex.setOnEditorActionListener(this.mEditHandler);

    this.mSeekBarRed.setOnSeekBarChangeListener(this.mSeekHandler);
    this.mSeekBarGreen.setOnSeekBarChangeListener(this.mSeekHandler);
    this.mSeekBarBlue.setOnSeekBarChangeListener(this.mSeekHandler);

    this.mButtonSet.setOnClickListener(this.mClickHandler);
    this.mButtonBlack.setOnClickListener(this.mClickHandler);
    this.mButtonWhite.setOnClickListener(this.mClickHandler);

    this.mButtonSaveBox1.setOnClickListener(this.mClickHandler);
    this.mButtonSaveBox2.setOnClickListener(this.mClickHandler);
    this.mButtonSaveBox3.setOnClickListener(this.mClickHandler);
    this.mButtonSaveBox4.setOnClickListener(this.mClickHandler);
    this.mButtonSaveBox5.setOnClickListener(this.mClickHandler);

    this.mButtonSaveBox1.setOnLongClickListener(this.mPokeHandler);
    this.mButtonSaveBox2.setOnLongClickListener(this.mPokeHandler);
    this.mButtonSaveBox3.setOnLongClickListener(this.mPokeHandler);
    this.mButtonSaveBox4.setOnLongClickListener(this.mPokeHandler);
    this.mButtonSaveBox5.setOnLongClickListener(this.mPokeHandler);

    this.mButtonBlack.setOnLongClickListener(mPokeHandler);
    this.mButtonWhite.setOnLongClickListener(mPokeHandler);

    this.mToggleButtonParadeMode.setOnCheckedChangeListener(this.mCheckHandler);
    this.mLinearLayoutApp.setOnLongClickListener(this.mPokeHandler);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      this.mSpinnerColorNames = (Spinner) findViewById(R.id.spinner_colornames);
      this.mSpinnerColorNames.setOnItemSelectedListener(this.mChoiceHandler);
    }

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
    editor.putInt(getString(R.string.prefkey_int_color_red), this.mColorRed);
    editor.putInt(getString(R.string.prefkey_int_color_green), this.mColorGreen);
    editor.putInt(getString(R.string.prefkey_int_color_blue), this.mColorBlue);

    // store parade mode status
    editor.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.mParadeMode);

    // store saved color preset values
    editor.putInt(getString(R.string.prefkey_int_colorpreset_one), this.mColorPreset1);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_two), this.mColorPreset2);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_three), this.mColorPreset3);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_four), this.mColorPreset4);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_five), this.mColorPreset5);

    editor.commit();
  }

  // SAVESTATE: Saves the app instance state
  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {

    super.onSaveInstanceState(savedInstanceState);

    // save RGB colors
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_red), this.mColorRed);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_green), this.mColorGreen);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_blue), this.mColorBlue);

    // save parade mode status
    savedInstanceState.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.mParadeMode);

    // save color preset values
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_one), this.mColorPreset1);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_two), this.mColorPreset2);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_three), this.mColorPreset3);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_four), this.mColorPreset4);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_five), this.mColorPreset5);
  }

  // GETSTATE: Gets the app instance state
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    // get RGB colors
    this.mColorRed = savedInstanceState.getInt(getString(R.string.prefkey_int_color_red));
    this.mColorGreen = savedInstanceState.getInt(getString(R.string.prefkey_int_color_green));
    this.mColorBlue = savedInstanceState.getInt(getString(R.string.prefkey_int_color_blue));

    // get parade mode status
    this.mParadeMode = savedInstanceState.getBoolean(getString(R.string.prefkey_bool_parade_mode));

    // get color preset values
    this.mColorPreset1 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_one));
    this.mColorPreset2 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_two));
    this.mColorPreset3 =
        savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_three));
    this.mColorPreset4 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_four));
    this.mColorPreset5 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_five));

    this.updateByRgb();
  }

  // updates color based on hexadecimal input
  private void updateByHex() {

    String hexString;
    int[] rgb = new int[3];

    hexString = this.mEditTextHex.getText().toString();
    rgb = hex2rgb(hexString);

    this.mColorRed = rgb[0];
    this.mColorGreen = rgb[1];
    this.mColorBlue = rgb[2];

    this.updateByRgb();
  }

  // updates color based on RGB input
  private void updateByRgb() {

    int r, g, b;

    r = this.mColorRed;
    g = this.mColorGreen;
    b = this.mColorBlue;

    this.mSeekBarRed.setProgress(r);
    this.mSeekBarGreen.setProgress(g);
    this.mSeekBarBlue.setProgress(b);

    this.mEditTextRed.setText("" + r);
    this.mEditTextGreen.setText("" + g);
    this.mEditTextBlue.setText("" + b);

    this.mEditTextHex.setText(this.rgb2hex(r, g, b));
    this.updateBackground();
  }

  // updates background to match color, keeps hashtag legible
  private void updateBackground() {

    int r, g, b;
    int contrastColor;
    String hexColor;

    r = this.mColorRed;
    g = this.mColorGreen;
    b = this.mColorBlue;

    contrastColor = this.getContrastColor(r, g, b);
    hexColor = this.mEditTextHex.getText().toString();

    this.mLinearLayoutApp.setBackgroundColor(Color.rgb(r, g, b));
    this.mTextViewHashTag.setTextColor(contrastColor);

    this.mTextViewColorName.setText("");

    // update X11 color names output (if any)
    if (this.mLinkedHashMapColorNames.containsKey(hexColor)) {
      this.mTextViewColorName.setText(this.mLinkedHashMapColorNames.get(hexColor));
      this.mTextViewColorName.setTextColor(contrastColor);
    }

    // update save boxes
    this.mButtonSaveBox1.setBackgroundColor(this.mColorPreset1);
    this.mButtonSaveBox2.setBackgroundColor(this.mColorPreset2);
    this.mButtonSaveBox3.setBackgroundColor(this.mColorPreset3);
    this.mButtonSaveBox4.setBackgroundColor(this.mColorPreset4);
    this.mButtonSaveBox5.setBackgroundColor(this.mColorPreset5);
  }

  // determines whether hashtag should be black or white
  private int getContrastColor(int r, int g, int b) {

    double rx, gx, bx;

    rx = 0.213 * r / 255;
    gx = 0.715 * g / 255;
    bx = 0.072 * b / 255;

    return (rx + gx + bx < 0.5) ? Color.WHITE : Color.BLACK;
  }

  // sets current color to black
  private void setToBlack() {

    this.mColorRed = 0;
    this.mColorGreen = 0;
    this.mColorBlue = 0;
    this.updateByRgb();
  }

  // sets current color to white
  private void setToWhite() {

    this.mColorRed = 255;
    this.mColorGreen = 255;
    this.mColorBlue = 255;
    this.updateByRgb();
  }

  // filters RGB color input value
  private int processRgbInput(String input) {

    int value = 0;

    try {

      value = Integer.parseInt(input);

      // value is bounded between 0 and 255
      this.filterRgbInput(value);

    } catch (NumberFormatException ex) {
      value = 0; // no problem. moving along.
    }

    return value;
  }

  // converts decimal number to nicely-formatted hexadecimal number
  private String dec2hex(int value) {

    String hex;

    try {

      // dec to hex
      hex = Integer.toHexString(value);

      // single-digit to double-digit hex
      hex = "00".substring(hex.length()) + hex;

      // uppercase HEX
      hex = hex.toUpperCase(Locale.US);

    } catch (NumberFormatException e) {

      // unlikely Exception. anyways, reset to [00].
      hex = "00";
    }

    return hex;
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

    } catch (IllegalArgumentException ex) {
      // no problem. just move on.
      rgb[0] = rgb[1] = rgb[2] = 0;
    }

    return rgb;
  }

  // converts directly from color to rgb array
  private Integer[] getRgbFromColor(int color) {

    Integer[] rgb = new Integer[3];
    rgb[0] = Color.red(color);
    rgb[1] = Color.green(color);
    rgb[2] = Color.blue(color);

    return rgb;
  }

  // converts directly from color to hex
  private String getHexFromColor(int color) {
    return "#" + dec2hex(Color.red(color)) + dec2hex(Color.green(color))
        + dec2hex(Color.blue(color));
  }

  // converts RGB color values into hexadecimal
  private String rgb2hex(int r, int g, int b) {
    return dec2hex(r) + "" + dec2hex(g) + "" + dec2hex(b);
  }

  // returns integers from 0 to 255.
  private int filterRgbInput(int value) {
    return Math.max(0, Math.min(255, value));
  }

  // returns export string from a color
  private String buildExportString(int color) {
    return Arrays.asList(getRgbFromColor(color)) + " " + getHexFromColor(color);
  }

  // exports palette with Intent, loading Chooser
  private void sendPaletteByIntent() {

    String str = "";
    str =
        buildExportString(mColorPreset1) + "; " + buildExportString(mColorPreset2) + "; "
            + buildExportString(mColorPreset3) + "; " + buildExportString(mColorPreset4) + "; "
            + buildExportString(mColorPreset5);

    // boilerplate intent code
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, str);
    sendIntent.setType("text/plain");
    startActivity(Intent.createChooser(sendIntent, "Send Palette to:"));
  }

  // activity data startup method
  private void letsgo() {

    // delimiter
    String delimiter = ":";
    String[] hexNameArray;

    for (String hexAndColorName : getResources().getStringArray(R.array.arr_x11_colornames_byhex)) {
      hexNameArray = hexAndColorName.split(delimiter);
      mLinkedHashMapColorNames.put(hexNameArray[0], hexNameArray[1]);
    }

  }

  /*
   * Event Listeners - Nice, clean way to respond to user wants and needs.
   */

  // handles keys
  private View.OnKeyListener mKeyHandler = new View.OnKeyListener() {

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

  // handles edits
  private TextView.OnEditorActionListener mEditHandler = new TextView.OnEditorActionListener() {

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
              case R.id.edittext_red:
                mColorRed = value;
                break;
              case R.id.edittext_green:
                mColorGreen = value;
                break;
              case R.id.edittext_blue:
                mColorBlue = value;
                break;
              case R.id.edittext_hextext:
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

  // handles checks and unchecks
  private CompoundButton.OnCheckedChangeListener mCheckHandler =
      new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton bv, boolean isChecked) {

          mParadeMode = isChecked;

          if (isChecked) {
            Toast.makeText(bv.getContext(), getResources().getString(R.string.str_parademode_tip),
                Toast.LENGTH_LONG).show();
          }

          updateByRgb();
        }
      };

  // handles focus changes
  private View.OnFocusChangeListener mFocusHandler = new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

      if (!hasFocus) {

        int value = 0;
        EditText et = (EditText) findViewById(v.getId());

        value = processRgbInput(et.getText().toString());

        // process input text
        et.setText(value + "");

        switch (v.getId()) {
          case R.id.edittext_red:
            mColorRed = value;
            break;
          case R.id.edittext_green:
            mColorGreen = value;
            break;
          case R.id.edittext_blue:
            mColorBlue = value;
            break;
          default:
            // do nothing.
            break;
        }

        updateByRgb();
      }
    }
  };

  // handles pokes
  private View.OnLongClickListener mPokeHandler = new View.OnLongClickListener() {

    @Override
    public boolean onLongClick(View v) {

      int id;
      int r, g, b, color;

      id = v.getId();

      switch (id) {

        case R.id.button_white:
          mColorPreset1 = Color.WHITE;
          mColorPreset2 = Color.WHITE;
          mColorPreset3 = Color.WHITE;
          mColorPreset4 = Color.WHITE;
          mColorPreset5 = Color.WHITE;
          updateByHex();

          Toast.makeText(getBaseContext(), getString(R.string.str_fadetowhite) + "",
              Toast.LENGTH_SHORT).show();

          break;
        case R.id.button_black:
          mColorPreset1 = Color.BLACK;
          mColorPreset2 = Color.BLACK;
          mColorPreset3 = Color.BLACK;
          mColorPreset4 = Color.BLACK;
          mColorPreset5 = Color.BLACK;
          updateByHex();

          Toast.makeText(getBaseContext(), getString(R.string.str_fadetoblack) + "",
              Toast.LENGTH_SHORT).show();
          break;
        case R.id.button_savebox1:
        case R.id.button_savebox2:
        case R.id.button_savebox3:
        case R.id.button_savebox4:
        case R.id.button_savebox5:

          r = mColorRed;
          g = mColorGreen;
          b = mColorBlue;

          String hex = rgb2hex(r, g, b);
          mEditTextHex.setText(hex);

          // get the specific button 'poked'
          Button btn = (Button) findViewById(id);
          btn.setTextColor(getContrastColor(r, g, b));

          color = Color.rgb(r, g, b);
          btn.setBackgroundColor(color);

          switch (id) {
            case R.id.button_savebox1:
              mColorPreset1 = color;
              break;
            case R.id.button_savebox2:
              mColorPreset2 = color;
              break;
            case R.id.button_savebox3:
              mColorPreset3 = color;
              break;
            case R.id.button_savebox4:
              mColorPreset4 = color;
              break;
            case R.id.button_savebox5:
              mColorPreset5 = color;
              break;
            default:
              // do nothing
              break;
          }

          updateByHex();
          Toast.makeText(getBaseContext(), getString(R.string.str_savebox_tip) + "",
              Toast.LENGTH_LONG).show();

          return true;
        case R.id.linearlayout_app:
          sendPaletteByIntent();
          return true;
        default:
          // do nothing
          break;
      }

      return false;
    }
  };

  // Handles clicks
  private View.OnClickListener mClickHandler = new View.OnClickListener() {

    @Override
    public void onClick(View v) {

      int color = 0;

      switch (v.getId()) {
        case R.id.button_savebox1:
          color = mColorPreset1;
          mColorRed = Color.red(color);
          mColorGreen = Color.green(color);
          mColorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.button_savebox2:
          color = mColorPreset2;
          mColorRed = Color.red(color);
          mColorGreen = Color.green(color);
          mColorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.button_savebox3:
          color = mColorPreset3;
          mColorRed = Color.red(color);
          mColorGreen = Color.green(color);
          mColorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.button_savebox4:
          color = mColorPreset4;
          mColorRed = Color.red(color);
          mColorGreen = Color.green(color);
          mColorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.button_savebox5:
          color = mColorPreset5;
          mColorRed = Color.red(color);
          mColorGreen = Color.green(color);
          mColorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.button_set:
          updateByHex();
          break;
        case R.id.button_black:
          setToBlack();
          break;
        case R.id.button_white:
          setToWhite();
          break;
        default:
          // do nothing
          break;
      }
    }
  };

  // Handles slips and slides
  private SeekBar.OnSeekBarChangeListener mSeekHandler = new SeekBar.OnSeekBarChangeListener() {

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
      // finger up, do nothing
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
      // finger down, do nothing
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

      // if user touch
      if (fromUser) {

        // if parade mode
        if (mParadeMode) {

          int delta = 0;

          switch (seekBar.getId()) {
            case R.id.seekbar_red:
              // delta = current progress - previous progress
              delta = progress - mColorRed;
              mColorRed = progress;
              mColorBlue = filterRgbInput(mColorBlue + delta);
              mColorGreen = filterRgbInput(mColorGreen + delta);
              break;
            case R.id.seekbar_green:
              delta = progress - mColorGreen;
              mColorRed = filterRgbInput(mColorRed + delta);
              mColorGreen = progress;
              mColorBlue = filterRgbInput(mColorBlue + delta);
              break;
            case R.id.seekbar_blue:
              delta = progress - mColorBlue;
              mColorRed = filterRgbInput(mColorRed + delta);
              mColorGreen = filterRgbInput(mColorGreen + delta);
              mColorBlue = progress;
              break;
            default:
              // do nothing
              break;
          }

        } else {

          switch (seekBar.getId()) {
            case R.id.seekbar_red:
              mColorRed = progress;
              break;
            case R.id.seekbar_green:
              mColorGreen = progress;
              break;
            case R.id.seekbar_blue:
              mColorBlue = progress;
              break;
            default:
              // do nothing
              break;
          }
        }
      }

      updateByRgb();
    }
  };

  // Handles spinner choices
  private AdapterView.OnItemSelectedListener mChoiceHandler =
      new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> av, View v, int pos, long id) {

          String str;
          String x11name;
          String hex;

          switch (av.getId()) {

            case R.id.spinner_colornames:

              if (pos > 0) {

                str = getResources().getStringArray(R.array.arr_x11_colornames_byalpha)[pos];
                x11name = str.split(":")[0];
                hex = str.split(":")[1];
                mEditTextHex.setText(hex);

                updateByHex();

                Toast.makeText(getBaseContext(),
                    x11name + " " + getResources().getString(R.string.str_colorname_selected),
                    Toast.LENGTH_SHORT).show();

              } else {

                Toast.makeText(getBaseContext(),
                    getResources().getString(R.string.str_colorname_tip), Toast.LENGTH_SHORT)
                    .show();
              }

            default:
              // do nothing, for now
              break;
          }
        }

        @Override
        public void onNothingSelected(AdapterView<?> av) {
          // leave empty
        }
      };

}
