import java.util.*;

class CRC {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Give size of input");
        int k = Integer.parseInt(scan.nextLine());

        double ber =  0.0001;
        System.out.println("BER: " + ber);

        System.out.println("Give divisor:");
        String divisor = scan.nextLine();

        int wrongMessages = 0;
        boolean errorAtMessage = false;

        for (int o = 0; o < 1000; o++) {

            int[] data = new int[k];
            for (int i = 0; i < k; i++) {
                data[i] = (int) Math.round(Math.random());
            }

            System.out.println(Arrays.toString(data));

            int[] divisorBits = new int[divisor.length()];
            for (int i = 0; i < divisor.length(); i++) {
                divisorBits[i] = Integer.parseInt(String.valueOf(divisor.charAt(i)));
            }

            System.out.println(Arrays.toString(divisorBits));

            // Divide the inputted data by the inputted divisor
            // Store the remainder that is returned by the method
            int remainder[] = divide(data, divisorBits);
            for (int i = 0; i < remainder.length - 1; i++) {
                System.out.print(remainder[i]);
            }
            System.out.println("\nThe CRC code generated is:");

            for (int i = 0; i < data.length; i++) {
                System.out.print(data[i]);
            }
            for (int i = 0; i < remainder.length - 1; i++) {
                System.out.print(remainder[i]);
            }
            System.out.println();

            int sent_data[] = new int[data.length + remainder.length];

            for (int i = 0; i < data.length; i++) {
                double randomNum = Math.random();

                if (randomNum < ber) {
                    if (data[i] == 1) {
                        data[i] = 0;
                        System.out.println("Error at bit " + i);
                        errorAtMessage = true;
                    } else {
                        data[i] = 1;
                        System.out.println("Error at bit " + i);
                        errorAtMessage = true;
                    }
                }
                sent_data[i] = data[i];
            }

            if(errorAtMessage){
                wrongMessages += 1;
            }

            for (int i = 0; i < remainder.length; i++) {
                double randomNum = Math.random();
                if (randomNum < ber) {
                    if (remainder[i] == 1) {
                        remainder[i] = 0;
                        System.out.println("Error at bit " + i);
                        errorAtMessage = true;
                    } else {
                        remainder[i] = 1;
                        System.out.println("Error at bit " + i);
                        errorAtMessage = true;
                    }
                }
                sent_data[data.length + i] = remainder[i];
            }
            receive(sent_data, divisorBits);
        }
        System.out.println("Out of 1000 messages\nMessages with error: " + (double) wrongMessages/ 10 + "% " + "(" +wrongMessages + ")");
    }

    static int[] divide(int old_data[], int divisor[]) {
        int remainder[] , i;
        int data[] = new int[old_data.length + divisor.length];
        System.arraycopy(old_data, 0, data, 0, old_data.length);
        // Remainder array stores the remainder
        remainder = new int[divisor.length];

        System.arraycopy(data, 0, remainder, 0, divisor.length);

        for(i=0 ; i < old_data.length ; i++) {
            if(remainder[0] == 1) {
                // We have to exor the remainder bits with divisor bits
                for(int j=1 ; j < divisor.length ; j++) {
                    remainder[j-1] = exor(remainder[j], divisor[j]);
                    System.out.print(remainder[j-1]);
                }
            }
            else {
                // We have to exor the remainder bits with 0
                for(int j=1 ; j < divisor.length ; j++) {
                    remainder[j-1] = exor(remainder[j], 0);
                    System.out.print(remainder[j-1]);
                }
            }
            // The last bit of the remainder will be taken from the data
            // This is the 'carry' taken from the dividend after every step
            // of division
            remainder[divisor.length-1] = data[i+divisor.length];
            System.out.println(remainder[divisor.length-1]);
        }
        return remainder;
    }

    static int exor(int a, int b) {

        if(a == b) {
            return 0;
        }
        return 1;
    }

    static void receive(int data[], int divisor[]) {
        // This is the receiver method
        int remainder[] = divide(data, divisor);

        for(int i=0 ; i < remainder.length ; i++) {
            if(remainder[i] != 0) {

                System.out.println("There is an error in received data...");
                return;
            }
        }

        System.out.println("Data was received without any error.");
    }
}