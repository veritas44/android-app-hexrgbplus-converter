/**
 * This is free and unencumbered software released into the public domain.
 * 
 * Anyone is free to copy, modify, publish, use, compile, sell, or distribute
 * this software, either in source code form or as a compiled binary, for any
 * purpose, commercial or non-commercial, and by any means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors of this
 * software dedicate any and all copyright interest in the software to the
 * public domain. We make this dedication for the benefit of the public at large
 * and to the detriment of our heirs and successors. We intend this dedication
 * to be an overt act of relinquishment in perpetuity of all present and future
 * rights to this software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <http://unlicense.org/>
 */

package pffy.mobile.hexrgbplus;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.app.Activity;
import android.content.ClipDescription;
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
 * @version 2.19 (r19)
 * @link https://github.com/pffy/android-hexrgbplus
 * @author https://github.com/pffy The Pffy Authors
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

  private LinkedHashMap<String, String> mLinkedHashMapColorNames =
      new LinkedHashMap<String, String>();

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
    this.letsgo();

    // Loads persistent data
    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);
    this.mColorRed = shpref.getInt(getString(R.string.prefkey_int_color_red), 0);
    this.mColorGreen = shpref.getInt(getString(R.string.prefkey_int_color_green), 0);
    this.mColorBlue = shpref.getInt(getString(R.string.prefkey_int_color_blue), 0);
    this.mParadeMode = shpref.getBoolean(getString(R.string.prefkey_bool_parade_mode), false);
    this.mToggleButtonParadeMode.setChecked(this.mParadeMode);

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


    // Event Listeners
    this.mEditTextHex.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextHex.setOnKeyListener(this.mKeyHandler);

    this.mEditTextRed.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextGreen.setOnFocusChangeListener(this.mFocusHandler);
    this.mEditTextBlue.setOnFocusChangeListener(this.mFocusHandler);

    this.mEditTextRed.setOnEditorActionListener(mEditHandler);
    this.mEditTextGreen.setOnEditorActionListener(mEditHandler);
    this.mEditTextBlue.setOnEditorActionListener(mEditHandler);
    
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

    this.updateByRgb();
  }

  // Saves persistent data
  @Override
  protected void onPause() {
    super.onPause();

    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = shpref.edit();

    editor.putInt(getString(R.string.prefkey_int_color_red), this.mColorRed);
    editor.putInt(getString(R.string.prefkey_int_color_green), this.mColorGreen);
    editor.putInt(getString(R.string.prefkey_int_color_blue), this.mColorBlue);

    editor.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.mParadeMode);

    editor.putInt(getString(R.string.prefkey_int_colorpreset_one), this.mColorPreset1);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_two), this.mColorPreset2);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_three), this.mColorPreset3);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_four), this.mColorPreset4);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_five), this.mColorPreset5);

    editor.commit();
  }

  // Saves instance state
  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);

    savedInstanceState.putInt(getString(R.string.prefkey_int_color_red), this.mColorRed);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_green), this.mColorGreen);
    savedInstanceState.putInt(getString(R.string.prefkey_int_color_blue), this.mColorBlue);

    savedInstanceState.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.mParadeMode);

    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_one), this.mColorPreset1);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_two), this.mColorPreset2);
    savedInstanceState
        .putInt(getString(R.string.prefkey_int_colorpreset_three), this.mColorPreset3);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_four), this.mColorPreset4);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_five), this.mColorPreset5);
  }

  // Loads instance state
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);

    this.mColorRed = savedInstanceState.getInt(getString(R.string.prefkey_int_color_red));
    this.mColorGreen = savedInstanceState.getInt(getString(R.string.prefkey_int_color_green));
    this.mColorBlue = savedInstanceState.getInt(getString(R.string.prefkey_int_color_blue));

    this.mParadeMode = savedInstanceState.getBoolean(getString(R.string.prefkey_bool_parade_mode));

    this.mColorPreset1 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_one));
    this.mColorPreset2 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_two));
    this.mColorPreset3 =
        savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_three));
    this.mColorPreset4 =
        savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_four));
    this.mColorPreset5 =
        savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_five));

    this.updateByRgb();
  }

  // Updates color based on hexadecimal input
  private void updateByHex() {

    String hexString;
    int[] rgb = new int[3];

    hexString = getHexInput();
    rgb = hex2rgb(hexString);

    this.mColorRed = rgb[0];
    this.mColorGreen = rgb[1];
    this.mColorBlue = rgb[2];

    this.updateByRgb();
  }

  // Returns hexadecimal string input
  private String getHexInput() {

    String str = getString(R.string.str_placeholder_hex);

    if (this.mEditTextHex != null) {
      str = this.mEditTextHex.getText().toString();

      if (!str.isEmpty()) {
        return str;
      }
    }

    return str;
  }

  // Updates color based on RGB input
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

  // Updates background to match color, keeps hashtag legible
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

    // update X11 color names output (if any)
    this.mTextViewColorName.setText("");
    if (this.mLinkedHashMapColorNames.containsKey(hexColor)) {
      this.mTextViewColorName.setText(this.mLinkedHashMapColorNames.get(hexColor));
      this.mTextViewColorName.setTextColor(contrastColor);
    }

    this.updatePalette();
  }

  // Updates palette of color presets
  private void updatePalette() {
    this.mButtonSaveBox1.setBackgroundColor(this.mColorPreset1);
    this.mButtonSaveBox2.setBackgroundColor(this.mColorPreset2);
    this.mButtonSaveBox3.setBackgroundColor(this.mColorPreset3);
    this.mButtonSaveBox4.setBackgroundColor(this.mColorPreset4);
    this.mButtonSaveBox5.setBackgroundColor(this.mColorPreset5);
  }

  // Determines whether hashtag (#) color should be black or white
  private int getContrastColor(int r, int g, int b) {

    double rx, gx, bx;

    rx = 0.213 * r / 255;
    gx = 0.715 * g / 255;
    bx = 0.072 * b / 255;

    return (rx + gx + bx < 0.5) ? Color.WHITE : Color.BLACK;
  }

  // Resets current color to black
  private void setToBlack() {
    this.mColorRed = 0;
    this.mColorGreen = 0;
    this.mColorBlue = 0;
    this.updateByRgb();
  }

  // Resets current color to white
  private void setToWhite() {
    this.mColorRed = 255;
    this.mColorGreen = 255;
    this.mColorBlue = 255;
    this.updateByRgb();
  }

  // Returns a bounded RGB color value from String input
  private int getRgbFromInput(String str) {
    return this.getBoundedRgbColorValue(this.getIntegerFromString(str));
  }

  // Returns a bounded RGB color value
  private int getBoundedRgbColorValue(int value) {
    return Math.max(0, Math.min(255, value));
  }

  // Returns an integer from a string input
  private int getIntegerFromString(String str) {
    try {
      return Integer.valueOf(str).intValue();
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }

  // Converts decimal number to nicely-formatted hexadecimal number
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
    } catch (StringIndexOutOfBoundsException sioobe) {
      hex = "00"; // more likely on strange input
    }

    return hex;
  }

  // Converts hexadecimal into RGB color values
  private int[] hex2rgb(String hex) {
    try {
      int color = Color.parseColor("#" + hex);
      return new int[] {Color.red(color), Color.green(color), Color.blue(color)};
    } catch (IllegalArgumentException ex) {
      return new int[] {0, 0, 0};
    }
  }

  // Converts from color to RGB Integer array
  private Integer[] getRgbFromColor(int color) {
    return new Integer[] {Color.red(color), Color.green(color), Color.blue(color)};
  }

  // Converts from color to hex
  private String getHexFromColor(int color) {
    return "#" + dec2hex(Color.red(color)) + dec2hex(Color.green(color))
        + dec2hex(Color.blue(color));
  }

  // Converts RGB color values into hexadecimal
  private String rgb2hex(int r, int g, int b) {
    return "" + dec2hex(r) + "" + dec2hex(g) + "" + dec2hex(b);
  }

  // Returns export string from a color
  private String buildExportString(int color) {
    return Arrays.asList(getRgbFromColor(color)) + " " + getHexFromColor(color);
  }

  // Exports palette with Intent, loading Chooser
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
    sendIntent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
    startActivity(Intent.createChooser(sendIntent, getString(R.string.str_action_share)));
  }

  // Loads important data
  private void letsgo() {

    String delimiter = ":";
    String[] hexNameArray;

    for (String hexAndColorName : getResources().getStringArray(R.array.arr_x11_colornames_byhex)) {
      hexNameArray = hexAndColorName.split(delimiter);
      mLinkedHashMapColorNames.put(hexNameArray[0], hexNameArray[1]);
    }
  }
  
  private void setRgbByView(View v) {
    
    int value = 0;
    EditText et = (EditText) findViewById(v.getId());
    value = getRgbFromInput(et.getText().toString());
    et.setText("" + value);

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
        // nothing
        break;
    }
  }


  /*
   * Event Listeners - Nice, clean way to respond to user wants and needs.
   */

  // Handles keys
  private View.OnKeyListener mKeyHandler = new View.OnKeyListener() {

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

      switch (keyCode) {
        case KeyEvent.KEYCODE_ENTER:
        case KeyEvent.KEYCODE_DPAD_CENTER:
          updateByHex();
          return true;
        default:
          // nothing
          break;
      }

      return false;
    }
  };

  // Handles checks
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

  // Handles edits
  private TextView.OnEditorActionListener mEditHandler = new TextView.OnEditorActionListener() {

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

      if (actionId == EditorInfo.IME_ACTION_DONE) {
        setRgbByView(v);
        updateByRgb();
      }
      return false;
    }
  };

  
  // Handles focus
  private View.OnFocusChangeListener mFocusHandler = new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

      if (!hasFocus) {
        setRgbByView(v);
        updateByRgb();
      }
    }
  };

  // Handles pokes
  private View.OnLongClickListener mPokeHandler = new View.OnLongClickListener() {

    @Override
    public boolean onLongClick(View v) {

      int id = v.getId();

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

          int r,
          g,
          b,
          color;

          r = mColorRed;
          g = mColorGreen;
          b = mColorBlue;

          String hex = rgb2hex(r, g, b);
          mEditTextHex.setText(hex);

          // get the specific button that was 'poked'
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
              // nothing
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
          // nothing
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
          // nothing
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
              mColorBlue = getBoundedRgbColorValue(mColorBlue + delta);
              mColorGreen = getBoundedRgbColorValue(mColorGreen + delta);
              break;
            case R.id.seekbar_green:
              delta = progress - mColorGreen;
              mColorRed = getBoundedRgbColorValue(mColorRed + delta);
              mColorGreen = progress;
              mColorBlue = getBoundedRgbColorValue(mColorBlue + delta);
              break;
            case R.id.seekbar_blue:
              delta = progress - mColorBlue;
              mColorRed = getBoundedRgbColorValue(mColorRed + delta);
              mColorGreen = getBoundedRgbColorValue(mColorGreen + delta);
              mColorBlue = progress;
              break;
            default:
              // nothing
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
              // nothing
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
              // nothing
              break;
          }
        }

        @Override
        public void onNothingSelected(AdapterView<?> av) {
          // leave empty
        }
      };

}
