package com.third;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    private EditText check;
    private EditText tax;
    private EditText taxAmount;
    private EditText subTotal;
    private EditText tip;
    private EditText tipAmount;
    private EditText total;

    private final BigDecimal HUNDRED = new BigDecimal("100");
    private final BigDecimal TIP = new BigDecimal(".15");
    private final BigDecimal TAX = new BigDecimal(".075");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = (EditText) findViewById(R.id.check);
        tax = (EditText) findViewById(R.id.tax);
        taxAmount = (EditText) findViewById(R.id.taxAmount);
        subTotal = (EditText) findViewById(R.id.subTotal);
        tip = (EditText) findViewById(R.id.tip);
        tipAmount = (EditText) findViewById(R.id.tipAmount);
        total = (EditText) findViewById(R.id.total);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void calcTip(View view){
        BigDecimal _check = getNumber( check );
        BigDecimal _tax = getNumber( tax );
        BigDecimal _taxAmount = getNumber( taxAmount );
        BigDecimal _subTotal = getNumber( subTotal );
        BigDecimal _tip = getNumber( tip );
        BigDecimal _tipAmount = getNumber( tipAmount );
        BigDecimal _total = getNumber( total );
        BigDecimal _t = BigDecimal.ZERO;


        try {
            if ( _tax.compareTo( BigDecimal.ZERO ) > 0 ) {
                //we have tax%

                _t = _tax.divide( HUNDRED, 5, BigDecimal.ROUND_HALF_UP );

            } else if( _taxAmount.compareTo( BigDecimal.ZERO ) > 0 ) {
                //we don't have tax% but we have tax amount

                if( _check.compareTo(BigDecimal.ZERO) > 0 ){
                    _subTotal = _check.add( _taxAmount );
                } else if( _subTotal.compareTo(BigDecimal.ZERO) > 0 ){
                    _check = _subTotal.subtract( _taxAmount );
                } else {
                    //neither check amount nor subtotal
                    return;
                }

                _t = _check.divide( _subTotal, 5, BigDecimal.ROUND_HALF_UP );
                _tax = _t.multiply( HUNDRED ).setScale( 2, BigDecimal.ROUND_HALF_UP );

            } else if ( _check.compareTo(BigDecimal.ZERO) > 0 && _subTotal.compareTo( _check ) > 0 ){

                _t = _check.divide(_subTotal, 5, BigDecimal.ROUND_HALF_UP);
                _tax = _t.multiply( HUNDRED ).setScale(2, BigDecimal.ROUND_HALF_UP);

            }


            if( _check.compareTo(BigDecimal.ZERO) == 0 && _subTotal.compareTo(BigDecimal.ZERO) > 0 ){
                _check = _subTotal.divide( BigDecimal.ONE.add( _t ), 5, BigDecimal.ROUND_HALF_UP);
            } else if( _check.compareTo(BigDecimal.ZERO) > 0 &&  _subTotal.compareTo(BigDecimal.ZERO) == 0 ) {
                _subTotal = _check.add( _check.multiply( _t) );
            }

            _taxAmount = _subTotal.subtract( _check );

            check.setText( _check.setScale( 2, BigDecimal.ROUND_HALF_UP ).toString());
            tax.setText( _tax.toString() );
            taxAmount.setText(_taxAmount.setScale( 2, BigDecimal.ROUND_HALF_UP ).toString());
            subTotal.setText( _subTotal.setScale( 2, BigDecimal.ROUND_HALF_UP ).toString() );


            if( _tipAmount.compareTo(BigDecimal.ZERO) == 0 ) {
                if( _tip.compareTo(BigDecimal.ZERO) > 0 ){
                    _tipAmount = _check.multiply( _tip.divide( HUNDRED, 5, BigDecimal.ROUND_HALF_UP ) );
                } else if( _total.compareTo(BigDecimal.ZERO) > 0 ){
                    _tipAmount = _total.subtract( _subTotal );
                    _tip = _tipAmount.divide( _check, 5, BigDecimal.ROUND_HALF_UP ).multiply( HUNDRED );
                }
            }

            if( _tip.compareTo(BigDecimal.ZERO) == 0 && _tipAmount.compareTo(BigDecimal.ZERO) > 0){
               _tip = _tipAmount.divide( _check, 5, BigDecimal.ROUND_HALF_UP ).multiply( HUNDRED );
            }


            if( _total.compareTo(BigDecimal.ZERO) == 0 ){
                _total = _subTotal.add( _tipAmount );
            }

            tip.setText( _tip.setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
            tipAmount.setText( _tipAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
            total.setText( _total.setScale(2, BigDecimal.ROUND_HALF_UP).toString() );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private BigDecimal getNumber( EditText text ){
        try {
            return new BigDecimal(text.getText().toString() );
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
