import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args)
    {
        int maxX = 200;
        int maxY = 700;
        String outputDir = "/Users/brendanreidy/Desktop/567 Final Project/TrainingRenders/TwoInputsTwoOutputs/";
        //Matrix2D mnistTrain = Matrix2D.loadFromFile("/Users/brendanreidy/IdeaProjects/NeuralNetwork/imageTrainingData.csv");
        Matrix2D mnistTrain  = Matrix2D.loadFromFile("basicNetwork.txt");

        float[][] trainLabels = new float[mnistTrain.length/2][];
        float[][] trainInputs = new float[mnistTrain.length/2][];

        for(int i=0; i<mnistTrain.length/2; i++)
        {
            trainLabels[i] = mnistTrain.getArrayAt((i*2)+1);
            trainInputs[i] = mnistTrain.getArrayAt((i*2));
        }
        //float[][] resizedInput = resizeArray(trainInputs);
        LinkedList<BufferedImage> images = new LinkedList<>();
        float[][] resizedInput = trainInputs;

        NeuralNetwork network = new NeuralNetwork();
        network.setTrainingData(resizedInput, trainLabels);
        network.addLayer(new PlaceHolder2D(resizedInput[0].length, 1));
        network.addLayer(new FullyConnected(1, "sigmoid"));
        network.addLayer(new FullyConnected(trainLabels[0].length, "sigmoid"));
        NetworkGraphics graphics = new NetworkGraphics(network);
        BufferedImage image;
        int sizeX = graphics.getWindowSizeX(1000);
        int sizeY = graphics.getWindowSizeY(750);
        for(int i=0; i<20; i++) {
            for(int j=0; j<resizedInput.length; j++)
            {
                network.setInput(resizedInput[j]);
                network.feedForward();
                network.propagateBack(trainLabels[j]);
                network.feedForward();
                image = graphics.drawNetwork();
                image = ImageUtils.resize(image, sizeX, sizeY);
                images.add(image);
                ImageUtils.display(image);
            }
        }
        int p = 0;
        int x = 0;
        int nextInterval = 0;
        /*
        for(int k=0; k<10000; k++) {
            for (int i = 0; i < resizedInput.length; i++) {
                if(p>=nextInterval) {
                    System.out.println(p);
                    network.setInput(resizedInput[7]);
                    network.feedForward();
                    if(p>=500)
                        nextInterval=nextInterval + 400;
                    else
                        nextInterval=nextInterval + 400;
                    image = graphics.drawNetwork();
                    //image = writeText(image, "Cost: " + network.getCost());
                    ImageUtils.saveImage(image,"", "Images_" + x + ".png");
                    image = ImageUtils.resize(image, sizeX, sizeY);
                    //images.add(image);
                    ImageUtils.display(image);
                    x++;
                }
                network.setInput(resizedInput[i]);
                network.feedForward();
                network.propagateBack(trainLabels[i]);
                p++;
            }
        }
        network.train();
        for(int i=0; i<25; i++){
            float[] input = {0,0,(float) i/25f};

            network.setInput(input);
            network.feedForward();
            image = graphics.drawNetwork();
            //ImageUtils.saveImage(image,"/Users/brendanreidy/Desktop/NNImages/", "Finished_Images_011_" + i + ".png");
            image = ImageUtils.resize(image, sizeX, sizeY);
            images.add(image);
            ImageUtils.display(image);
        }
        for(int i=0; i<25; i++){
            float[] input = {0,(float) i/25f,1};

            network.setInput(input);
            network.feedForward();
            image = graphics.drawNetwork();
            //ImageUtils.saveImage(image,"/Users/brendanreidy/Desktop/NNImages/", "Finished_Images_011_" + i + ".png");
            image = ImageUtils.resize(image, sizeX, sizeY);
            images.add(image);
            ImageUtils.display(image);
        }
        for(int i=0; i<25; i++){
            float[] input = {(float) i/25f, 1,1};

            network.setInput(input);
            network.feedForward();
            image = graphics.drawNetwork();
            //ImageUtils.saveImage(image,"/Users/brendanreidy/Desktop/NNImages/", "Finished_Images_111_" + i + ".png");
            image = ImageUtils.resize(image, sizeX, sizeY);
            images.add(image);
            ImageUtils.display(image);
        }
        */
        int k=0;
        boolean direction = true;
        long timer = System.currentTimeMillis();
        while(true)
        {
            while(timer + 100 > System.currentTimeMillis()){

            }
            timer = System.currentTimeMillis();
            if(k>=images.size()-2) {
                direction = !direction;
            }
            if(k==0 && !direction){
                direction = true;
            }
            ImageUtils.display(images.get(k));
            if(direction)
                k++;
            else
                k--;
        }
        //*/
    }
    public static int fromOneHotToNumber(float[] oneHot)
    {
        for(int i=0; i<oneHot.length; i++)
        {
            if(oneHot[i]>0.5)
                return i;
        }
        return -1;
    }
    public static BufferedImage writeText(BufferedImage image, String text)
    {
        int size = 200;
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial Black", Font.BOLD, size));
        graphics.drawString(text, image.getWidth() - 2000, 280);
        return image;
    }
    public static float[][] resizeArray(float[][] arr)
    {
        float[][] returnValue = new float[arr.length][];
        for(int i=0; i<arr.length; i++)
        {
           float[][] projection2D = new float[28][28];
           int x=0;
           int y=0;
           for(int j=0; j<arr[i].length; j++)
           {
               if(x>=28){
                   x=0;
                   y++;
               }
               projection2D[x][y] = arr[i][j];
               x++;
           }
           float[][] cropped2DProjection = new float[20][20];
           for(x=4; x<projection2D.length - 4; x++)
           {
               for(y=4; y<projection2D.length - 4; y++)
               {
                   cropped2DProjection[x-4][y-4] = projection2D[y][x];
               }
           }
           float[][] rescaled2DProjection = new float[10][10];
           for(x=0; x<rescaled2DProjection.length; x++)
           {
               for(y=0; y<rescaled2DProjection.length; y++)
               {
                   float pix1 = cropped2DProjection[x*2][y*2];
                   float pix2 = cropped2DProjection[x*2+1][y*2];
                   float pix3 = cropped2DProjection[x*2][y*2+1];
                   float pix4 = cropped2DProjection[x*2+1][y*2+1];
                   float avg = (pix1 + pix2 + pix3 + pix4)/4f;
                   rescaled2DProjection[x][y] = avg;
               }
           }
           float[] rescaled1DProjection = new float[rescaled2DProjection.length*rescaled2DProjection.length];
           int k=0;
           for(x=0; x<rescaled2DProjection.length; x++)
           {
               for(y=0; y<rescaled2DProjection.length; y++)
               {
                   rescaled1DProjection[k] = rescaled2DProjection[x][y];
                   k++;
               }
           }
           returnValue[i] = rescaled1DProjection;
        }
        return returnValue;
    }
}
