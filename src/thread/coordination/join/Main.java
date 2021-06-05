package thread.coordination.join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        var inputNumbers = Arrays.asList(0L,3435L, 35435L, 2324L, 4656L, 23L, 235L, 5566L);
        var threads = new ArrayList<FactorialThread>();

        inputNumbers.forEach(n -> threads.add(new FactorialThread(n)));
        threads.forEach(t -> t.start());

        threads.forEach(t -> {
            try {
                // You are saying that the maximum wait time is gonna be 2 sec.
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        for(int i = 0; i < inputNumbers.size(); i++){
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished){
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            }
            else{
                System.out.println("Calculation for " + inputNumbers.get(i) + " is still running");
            }
        }

    }

    public static class FactorialThread extends Thread{
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(final long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            result = calculateFactorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger calculateFactorial(final long n) {
            BigInteger tempResult = BigInteger.ONE;
            for (long i = n; i > 0; i--){
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public BigInteger getResult() {
            return result;
        }

        public boolean isFinished() {
            return isFinished;
        }

    }
}
