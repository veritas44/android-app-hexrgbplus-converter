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
 * @version 2.10 (r10)
 * @link https://github.com/pffy/
 * @author The Pffy Authors
 */

public class HexRgbPlusActivity extends Activity {

  private int colorRed = 0;
  private int colorGreen = 0;
  private int colorBlue = 0;

  private boolean paradeMode = false;

  private LinearLayout backdrop;

  private TextView tv_hashtag;
  private TextView tv_colorname;

  private Button btn_set;
  private Button btn_black;
  private Button btn_white;

  private EditText et_r;
  private EditText et_g;
  private EditText et_b;
  private EditText et_hex;

  private SeekBar sb_r;
  private SeekBar sb_g;
  private SeekBar sb_b;

  private ToggleButton tgb;

  private Button btn_savebox1;
  private Button btn_savebox2;
  private Button btn_savebox3;
  private Button btn_savebox4;
  private Button btn_savebox5;

  private LinkedHashMap<String, String> colorNames = new LinkedHashMap<String, String>();

  private int defaultColor = Color.WHITE;

  private int colorPreset1 = this.defaultColor;
  private int colorPreset2 = this.defaultColor;
  private int colorPreset3 = this.defaultColor;
  private int colorPreset4 = this.defaultColor;
  private int colorPreset5 = this.defaultColor;

  private Spinner sp_colornames;

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

    this.tgb = (ToggleButton) findViewById(R.id.tgbtn_parade_mode);


    // GETDATA: load color names
    this.letsgo();

    // GETPREFS: get persistent preferences
    SharedPreferences shpref = this.getPreferences(Context.MODE_PRIVATE);

    // get stored RGB values
    this.colorRed = shpref.getInt(getString(R.string.prefkey_int_color_red), 0);
    this.colorGreen = shpref.getInt(getString(R.string.prefkey_int_color_green), 0);
    this.colorBlue = shpref.getInt(getString(R.string.prefkey_int_color_blue), 0);

    // get stored parade mode status
    this.paradeMode = shpref.getBoolean(getString(R.string.prefkey_bool_parade_mode), false);

    this.tgb.setChecked(this.paradeMode);

    // get stored color preset values
    this.colorPreset1 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_one), this.defaultColor);
    this.colorPreset2 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_two), this.defaultColor);
    this.colorPreset3 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_three), this.defaultColor);
    this.colorPreset4 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_four), this.defaultColor);
    this.colorPreset5 =
        shpref.getInt(getString(R.string.prefkey_int_colorpreset_five), this.defaultColor);

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

    this.btn_savebox1.setOnClickListener(this.clickHandler);
    this.btn_savebox2.setOnClickListener(this.clickHandler);
    this.btn_savebox3.setOnClickListener(this.clickHandler);
    this.btn_savebox4.setOnClickListener(this.clickHandler);
    this.btn_savebox5.setOnClickListener(this.clickHandler);

    this.btn_savebox1.setOnLongClickListener(this.pokeHandler);
    this.btn_savebox2.setOnLongClickListener(this.pokeHandler);
    this.btn_savebox3.setOnLongClickListener(this.pokeHandler);
    this.btn_savebox4.setOnLongClickListener(this.pokeHandler);
    this.btn_savebox5.setOnLongClickListener(this.pokeHandler);

    this.btn_black.setOnLongClickListener(pokeHandler);
    this.btn_white.setOnLongClickListener(pokeHandler);

    this.tgb.setOnCheckedChangeListener(this.checkHandler);
    this.backdrop.setOnLongClickListener(this.pokeHandler);

    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      this.sp_colornames = (Spinner) findViewById(R.id.sp_colornames);
      this.sp_colornames.setOnItemSelectedListener(this.choiceHandler);
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
    editor.putInt(getString(R.string.prefkey_int_color_red), this.colorRed);
    editor.putInt(getString(R.string.prefkey_int_color_green), this.colorGreen);
    editor.putInt(getString(R.string.prefkey_int_color_blue), this.colorBlue);

    // store parade mode status
    editor.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.paradeMode);

    // store saved color preset values
    editor.putInt(getString(R.string.prefkey_int_colorpreset_one), this.colorPreset1);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_two), this.colorPreset2);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_three), this.colorPreset3);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_four), this.colorPreset4);
    editor.putInt(getString(R.string.prefkey_int_colorpreset_five), this.colorPreset5);

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

    // save parade mode status
    savedInstanceState.putBoolean(getString(R.string.prefkey_bool_parade_mode), this.paradeMode);

    // save color preset values
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_one), this.colorPreset1);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_two), this.colorPreset2);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_three), this.colorPreset3);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_four), this.colorPreset4);
    savedInstanceState.putInt(getString(R.string.prefkey_int_colorpreset_five), this.colorPreset5);
  }

  // GETSTATE: Gets the app instance state
  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {

    super.onRestoreInstanceState(savedInstanceState);

    // get RGB colors
    this.colorRed = savedInstanceState.getInt(getString(R.string.prefkey_int_color_red));
    this.colorGreen = savedInstanceState.getInt(getString(R.string.prefkey_int_color_green));
    this.colorBlue = savedInstanceState.getInt(getString(R.string.prefkey_int_color_blue));

    // get parade mode status
    this.paradeMode = savedInstanceState.getBoolean(getString(R.string.prefkey_bool_parade_mode));

    // get color preset values
    this.colorPreset1 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_one));
    this.colorPreset2 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_two));
    this.colorPreset3 =
        savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_three));
    this.colorPreset4 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_four));
    this.colorPreset5 = savedInstanceState.getInt(getString(R.string.prefkey_int_colorpreset_five));

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

    this.updateByRgb();
  }

  // updates color based on RGB input
  private void updateByRgb() {

    int r, g, b;

    r = this.colorRed;
    g = this.colorGreen;
    b = this.colorBlue;

    this.sb_r.setProgress(r);
    this.sb_g.setProgress(g);
    this.sb_b.setProgress(b);

    this.et_r.setText("" + r);
    this.et_g.setText("" + g);
    this.et_b.setText("" + b);

    this.et_hex.setText(this.rgb2hex(r, g, b));
    this.updateBackground();
  }

  // updates background to match color, keeps hashtag legible
  private void updateBackground() {

    int r, g, b;
    int contrastColor;
    String hexColor;

    r = this.colorRed;
    g = this.colorGreen;
    b = this.colorBlue;

    contrastColor = this.getContrastColor(r, g, b);
    hexColor = this.et_hex.getText().toString();

    this.backdrop.setBackgroundColor(Color.rgb(r, g, b));
    this.tv_hashtag.setTextColor(contrastColor);

    this.tv_colorname.setText("");

    // update X11 color names output (if any)
    if (this.colorNames.containsKey(hexColor)) {
      this.tv_colorname.setText(this.colorNames.get(hexColor));
      this.tv_colorname.setTextColor(contrastColor);
    }

    // update save boxes
    this.btn_savebox1.setBackgroundColor(this.colorPreset1);
    this.btn_savebox2.setBackgroundColor(this.colorPreset2);
    this.btn_savebox3.setBackgroundColor(this.colorPreset3);
    this.btn_savebox4.setBackgroundColor(this.colorPreset4);
    this.btn_savebox5.setBackgroundColor(this.colorPreset5);
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
        buildExportString(colorPreset1) + "; " + buildExportString(colorPreset2) + "; "
            + buildExportString(colorPreset3) + "; " + buildExportString(colorPreset4) + "; "
            + buildExportString(colorPreset5);

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
      colorNames.put(hexNameArray[0], hexNameArray[1]);
    }

  }

  /*
   * Event Listeners - Nice, clean way to respond to user wants and needs.
   */

  // handles keys
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

  // handles edits
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

  // handles checks and unchecks
  private CompoundButton.OnCheckedChangeListener checkHandler =
      new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton bv, boolean isChecked) {

          paradeMode = isChecked;

          if (isChecked) {
            Toast.makeText(bv.getContext(), getResources().getString(R.string.str_parademode_tip),
                Toast.LENGTH_LONG).show();
          }

          updateByRgb();
        }
      };

  // handles focus changes
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

  // handles pokes
  private View.OnLongClickListener pokeHandler = new View.OnLongClickListener() {

    @Override
    public boolean onLongClick(View v) {

      int id;
      int r, g, b, color;

      id = v.getId();

      switch (id) {

        case R.id.btn_white:
          colorPreset1 = Color.WHITE;
          colorPreset2 = Color.WHITE;
          colorPreset3 = Color.WHITE;
          colorPreset4 = Color.WHITE;
          colorPreset5 = Color.WHITE;
          updateByHex();
          break;
        case R.id.btn_black:
          colorPreset1 = Color.BLACK;
          colorPreset2 = Color.BLACK;
          colorPreset3 = Color.BLACK;
          colorPreset4 = Color.BLACK;
          colorPreset5 = Color.BLACK;
          updateByHex();
          break;
        case R.id.btn_savebox1:
        case R.id.btn_savebox2:
        case R.id.btn_savebox3:
        case R.id.btn_savebox4:
        case R.id.btn_savebox5:

          r = colorRed;
          g = colorGreen;
          b = colorBlue;

          String hex = rgb2hex(r, g, b);
          et_hex.setText(hex);

          // get the specific button 'poked'
          Button btn = (Button) findViewById(id);
          btn.setTextColor(getContrastColor(r, g, b));

          color = Color.rgb(r, g, b);
          btn.setBackgroundColor(color);

          switch (id) {
            case R.id.btn_savebox1:
              colorPreset1 = color;
              break;
            case R.id.btn_savebox2:
              colorPreset2 = color;
              break;
            case R.id.btn_savebox3:
              colorPreset3 = color;
              break;
            case R.id.btn_savebox4:
              colorPreset4 = color;
              break;
            case R.id.btn_savebox5:
              colorPreset5 = color;
              break;
            default:
              // do nothing
              break;
          }

          updateByHex();
          Toast.makeText(getBaseContext(), getString(R.string.str_savebox_tip) + "",
              Toast.LENGTH_LONG).show();

          return true;
        case R.id.linlay_background:
          sendPaletteByIntent();
          return true;
        default:
          // do nothing
          break;
      }

      return false;
    }
  };

  // handles clicks
  private View.OnClickListener clickHandler = new View.OnClickListener() {

    @Override
    public void onClick(View v) {

      int color = 0;

      switch (v.getId()) {
        case R.id.btn_savebox1:
          color = colorPreset1;
          colorRed = Color.red(color);
          colorGreen = Color.green(color);
          colorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.btn_savebox2:
          color = colorPreset2;
          colorRed = Color.red(color);
          colorGreen = Color.green(color);
          colorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.btn_savebox3:
          color = colorPreset3;
          colorRed = Color.red(color);
          colorGreen = Color.green(color);
          colorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.btn_savebox4:
          color = colorPreset4;
          colorRed = Color.red(color);
          colorGreen = Color.green(color);
          colorBlue = Color.blue(color);
          updateByRgb();
          break;
        case R.id.btn_savebox5:
          color = colorPreset5;
          colorRed = Color.red(color);
          colorGreen = Color.green(color);
          colorBlue = Color.blue(color);
          updateByRgb();
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

  // handles slips and slides
  private SeekBar.OnSeekBarChangeListener seekHandler = new SeekBar.OnSeekBarChangeListener() {

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
        if (paradeMode) {
          
          int delta = 0;

          switch (seekBar.getId()) {
            case R.id.sb_red:
              // delta = current progress - previous progress
              delta = progress - colorRed;
              colorRed = progress;
              colorBlue = filterRgbInput(colorBlue + delta);
              colorGreen = filterRgbInput(colorGreen + delta);
              break;
            case R.id.sb_green:
              delta = progress - colorGreen;
              colorRed = filterRgbInput(colorRed + delta);
              colorGreen = progress;
              colorBlue = filterRgbInput(colorBlue + delta);
              break;
            case R.id.sb_blue:
              delta = progress - colorBlue;
              colorRed = filterRgbInput(colorRed + delta);
              colorGreen = filterRgbInput(colorGreen + delta);
              colorBlue = progress;
              break;
            default:
              // do nothing
              break;
          }

        } else {

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
        }
      }

      updateByRgb();
    }
  };

  // handles spinner choices
  private AdapterView.OnItemSelectedListener choiceHandler =
      new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> av, View v, int pos, long id) {

          String str;
          String x11name;
          String hex;

          switch (av.getId()) {

            case R.id.sp_colornames:

              if (pos > 0) {

                str = getResources().getStringArray(R.array.arr_x11_colornames_byalpha)[pos];
                x11name = str.split(":")[0];
                hex = str.split(":")[1];
                et_hex.setText(hex);

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
