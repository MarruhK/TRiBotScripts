package scripts.gengarlibrary.algorithms;

import java.util.Random;

public class NormalDistribution
{
    /**
     * Generates a random number following a normal distribution
     *
     * If a value is found to be outside the range of the values, it will be omitted and will be re-generated.
     * @param values
     * @param min
     * @param max
     * @return
     */
    public static int getGeneratedRandomNormalValue(double[] values, int min, int max)
    {
        Random rand = new Random();

        double mean = findMean(values);
        double standardDeviation = findStandardDeviation(values, mean);

        int generatedRandomNormalValue = (int) Math.round(rand.nextGaussian() * standardDeviation + mean);

        // Fail safe ot ensure valid number is chosen
        while (generatedRandomNormalValue > max || generatedRandomNormalValue < min)
        {
            generatedRandomNormalValue = (int) Math.round(rand.nextGaussian() * standardDeviation + mean);
        }

        System.out.println("The generated normal value determined was: " + generatedRandomNormalValue);

        return generatedRandomNormalValue;
    }

    private static double findMean(double[] values)
    {
        double sum = 0;

        for (double val : values)
        {
            sum += val;
        }

        return sum / (1.0 * values.length);
    }

    private static double findStandardDeviation(double[] values, double mean)
    {
        // 1) Work out the Mean (the simple average of the numbers)

        // 2) Then for each number: subtract the Mean and square the result
        double[] newValues = new double[values.length];

        for (int i = 0; i < values.length; i++)
        {
            newValues[i] = Math.pow((values[i] - mean), 2);
        }

        // 3) Then work out the mean of those squared differences.
        double newMean = findMean(newValues);

        // 4) Take the square root of that and we are done!
        return Math.sqrt(newMean);
    }

    /**
     * Simply a test class to verify that the formula used works. This is useless for the implementation.
     * @param initialData
     */
    private void plotValues(double[] initialData, int min, int max)
    {
        // use last value i.e. values[30] for other vals.
        int[] values = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        for (int i = 0; i < 1000; i++)
        {
            int randomNormalDistValue = getGeneratedRandomNormalValue(initialData, min, max);

            while (1 > randomNormalDistValue || 30 < randomNormalDistValue)
            {
                randomNormalDistValue = getGeneratedRandomNormalValue(initialData, min, max);
            }

            switch (randomNormalDistValue)
            {
                case 1:
                    values[0]++;
                    break;
                case 2:
                    values[1]++;
                    break;
                case 3:
                    values[2]++;
                    break;
                case 4:
                    values[3]++;
                    break;
                case 5:
                    values[4]++;
                    break;
                case 6:
                    values[5]++;
                    break;
                case 7:
                    values[6]++;
                    break;
                case 8:
                    values[7]++;
                    break;
                case 9:
                    values[8]++;
                    break;
                case 10:
                    values[9]++;
                    break;
                case 11:
                    values[10]++;
                    break;
                case 12:
                    values[11]++;
                    break;
                case 13:
                    values[12]++;
                    break;
                case 14:
                    values[13]++;
                    break;
                case 15:
                    values[14]++;
                    break;
                case 16:
                    values[15]++;
                    break;
                case 17:
                    values[16]++;
                    break;
                case 18:
                    values[17]++;
                    break;
                case 19:
                    values[18]++;
                    break;
                case 20:
                    values[19]++;
                    break;
                case 21:
                    values[20]++;
                    break;
                case 22:
                    values[21]++;
                    break;
                case 23:
                    values[22]++;
                    break;
                case 24:
                    values[23]++;
                    break;
                case 25:
                    values[24]++;
                    break;
                case 26:
                    values[25]++;
                    break;
                case 27:
                    values[26]++;
                    break;
                case 28:
                    values[27]++;
                    break;
                case 29:
                    values[28]++;
                    break;
                case 30:
                    values[29]++;
                    break;
                default:
                    System.out.println("Default triggered, value = " + randomNormalDistValue);
                    values[30]++;
                    break;
            }
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for (int i = 1; i <= values.length; i++)
        {
            System.out.println("Value of [" + i + "] is : " + values[i-1]);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
}
