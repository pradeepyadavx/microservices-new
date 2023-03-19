package com.pradeep.productservice.services;



import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class HMACUtil {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    public static String hmacWithJava(String algorithm, String data, String key)
        throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }


    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte h : hash) {
          String hex = Integer.toHexString(0xff & h);
          if (hex.length() == 1)
            hexString.append('0');
          hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String data(){
        return """
            {"event_id":"c2bc5357-9cfc-4428-bbde-117b9efc064c","event_type":"transaction.auth.success","event_body":{"merchant":{"id":"840984","name":"Ticketmaster"},"features":{"is_test_mode":true},"transaction_id":"8029379273","transaction_type":"cc","condition":"pending","processor_id":"ccprocessorb","ponumber":"","order_description":"","order_id":"ac0dfacd-eb65-4cdc-8964-000000000000","customerid":"","customertaxid":"","website":"","shipping":"0.00","currency":"USD","tax":"0.00","surcharge":"","cash_discount":"","tip":"","requested_amount":"100.00","shipping_carrier":"","tracking_number":"","shipping_date":"","partial_payment_id":"","partial_payment_balance":"","platform_id":"ac0dfacd-eb65-4cdc-8964-000000000000","authorization_code":"123456","social_security_number":"","drivers_license_number":"","drivers_license_state":"","drivers_license_dob":"","duty_amount":"0.00","discount_amount":"0.00","national_tax_amount":"0.00","summary_commodity_code":"","alternate_tax_amount":"0.00","vat_tax_amount":"0.00","vat_tax_rate":"0.00","billing_address":{"first_name":"JOHN Q","last_name":"SMITH","address_1":"","address_2":"","company":"","city":"","state":"","postal_code":"","country":"","email":"","phone":"","cell_phone":"","fax":""},"shipping_address":{"first_name":"","last_name":"","address_1":"","address_2":"","company":"","city":"","state":"","postal_code":"","country":"","email":"","phone":"","fax":""},"card":{"cc_number":"411111******1111","cc_exp":"1229","cavv":"","cavv_result":"","xid":"","eci":"","avs_response":"","csc_response":"","cardholder_auth":"","cc_start_date":"","cc_issue_number":"","card_balance":"","card_available_balance":"","entry_mode":"","cc_bin":"","cc_type":"Visa","feature_token":""},"emv_meta_data":{"customer_verification_method":"online_pin","application_id":"A0000000031010","application_label":"VISA CREDIT","application_preferred_name":"CREDITO DE VISA","transaction_status_info":"E800","application_pan_sequence_number":"01"},"merchant_defined_fields":{},"action":{"amount":"100.00","action_type":"auth","date":"2023-02-01T23:52:03","success":"1","ip_address":"","source":"","api_method":"","username":"","response_text":"","response_code":"","processor_response_text":"","tap_to_mobile":false,"processor_response_code":"","device_license_number":"","device_nickname":""}}}
            """;
    }

    public static void main(String[] args) {

        try {
            String signingKey = "2E35B2346E0F07F6379C14338EEBFA57";
            InputStreamReader isr = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(isr);

            String webhookBody =data();

            String sigHeader = "t=1675313523,s=79e1da57a9b0e286737a65fe284a3e4a943cfccb287fe35392c03051d4621d6a";

            if (sigHeader == null || sigHeader.length() < 1) {
                throw new Exception("invalid webhook - signature header missing");
            }

            String nonce = null;
            String signature = null;
            String[] headerParts = sigHeader.split(",");
            for (String headerPart : headerParts) {
                String[] headerValues = headerPart.split("=");
                if (headerValues[0].equals("t")) {
                    nonce = headerValues[1];
                } else if (headerValues[0].equals("s")) {
                    signature = headerValues[1];
                }
            }

            String data=nonce + "." + webhookBody;

            if (nonce == null || signature == null) {
                throw new Exception("unrecognized webhook signature format");
            }


            String hashKey= hmacWithJava(HMAC_SHA256_ALGORITHM,data,signingKey);
            System.out.println(hashKey);
            System.out.println(signature);
            if (!hashKey.equals(signature)) {
                throw new Exception("invalid webhook - invalid signature, cannot verify sender");
            }

            // webhook is now verified to have been sent by us, continue processing

            System.out.println("webhook is verified");
            System.out.println(webhookBody);
        } catch (Exception e) {
            System.out.println("error: " + e);
        }


    }
}