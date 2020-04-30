import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Main2 {
    public static void main(String[] args) {
        String outputDir = "/Users/brendanreidy/Desktop/567 Final Project/TrainingRenders/TwoInputsTwoOutputs/";
        //Matrix2D mnistTrain = Matrix2D.loadFromFile("/Users/brendanreidy/IdeaProjects/NeuralNetwork/imageTrainingData.csv");
        Matrix2D mnistTrain  = Matrix2D.loadFromFile("twoBitAdderTrainingData.txt");

        float[][] trainLabels = new float[mnistTrain.length / 2][];
        float[][] trainInputs = new float[mnistTrain.length / 2][];

        for (int i = 0; i < mnistTrain.length / 2; i++) {
            trainLabels[i] = mnistTrain.getArrayAt((i * 2) + 1);
            trainInputs[i] = mnistTrain.getArrayAt((i * 2));
        }
        //float[][] resizedInput = trainInputs;
        float[][] resizedInput = resizeArray(trainInputs);


        LinkedList<BufferedImage> images = new LinkedList<>();

        NeuralNetwork network = new NeuralNetwork();
        network.setTrainingData(resizedInput, trainLabels);
        network.addLayer(new PlaceHolder2D(14, 14));
        FullyConnected fc1 = new FullyConnected(5, "sigmoid");
        FullyConnected fc2 = new FullyConnected(5, "sigmoid");
        FullyConnected fc3 = new FullyConnected(10, "sigmoid");
        network.addLayer(fc1);
        network.addLayer(fc2);
        network.addLayer(fc3);
        Network2DGraphics graphics = new Network2DGraphics(network);
        int sizeX = graphics.getWindowSizeX(1000);
        int sizeY = graphics.getWindowSizeY(750);
        BufferedImage image;
        for(int i=0; i<100; i++)
        {
            network.train();
            int m = (int) (Math.random()*3);
            network.feedForward();
            image = graphics.drawNetwork();
            image = ImageUtils.resize(image, sizeX, sizeY);
            images.add(image);
            ImageUtils.display(image);
            if(i%15==0) {
                if(m==1) {

                }else if(m==2) {
                    for (int x = 0; x < fc2.size(); x++) {
                        for (int y = 0; y < fc1.size(); y++) {
                            fc2.weights[x][y] = 0;
                        }
                    }
                }else {
                    for (int x = 0; x < fc3.size(); x++) {
                        for (int y = 0; y < fc2.size(); y++) {
                            fc3.weights[x][y] = 0;
                        }
                    }
                }
                network.feedForward();
                image = graphics.drawNetwork();
                image = ImageUtils.resize(image, sizeX, sizeY);
                images.add(image);
                ImageUtils.display(image);
            }
        }
        image = graphics.drawNetwork();
        image = ImageUtils.resize(image, sizeX, sizeY);
        images.add(image);
        for(int j=0; j<trainInputs.length/10; j++)
        {
            network.setInput(resizedInput[j]);
            network.feedForward();
            if(j<20 || j % 100==0) {
                image = graphics.drawNetwork();
                image = ImageUtils.resize(image, sizeX, sizeY);
                images.add(image);
                ImageUtils.display(image);
                //System.out.println(network.getCost());
            }
            network.propagateBack(trainLabels[j]);
        }
        image = graphics.drawNetwork();
        image = ImageUtils.resize(image, sizeX, sizeY);
        images.add(image);
        System.out.println(network.getCost());
        image = graphics.drawNetwork();
        image = ImageUtils.resize(image, sizeX, sizeY);
        images.add(image);
        for(int j=0; j<trainInputs.length/5; j++)
        {
            network.setInput(resizedInput[j]);
            network.feedForward();
            if(j<20 || j % 100==0) {
                image = graphics.drawNetwork();
                image = ImageUtils.resize(image, sizeX, sizeY);
                images.add(image);
                ImageUtils.display(image);
                System.out.println(network.getCost());
            }
            network.propagateBack(trainLabels[j]);
        }

        /*
        for(int i=0; i<15; i++)
        {
            image = graphics.drawNetwork();
            image = ImageUtils.resize(image, sizeX, sizeY);
            images.add(image);
            ImageUtils.display(image);
            System.out.println(network.getCost());
            for(int j=0; j<trainInputs.length; j++)
            {
                network.setInput(resizedInput[j]);
                network.feedForward();
                if(j<20 || j % (100*(i+1))==0) {
                    image = graphics.drawNetwork();
                    image = ImageUtils.resize(image, sizeX, sizeY);
                    images.add(image);
                    ImageUtils.display(image);
                    //System.out.println(network.getCost());
                }
                network.propagateBack(trainLabels[j]);
            }
            System.out.println(i);
        }
        */
        network.setInput(resizedInput[55]);
        network.feedForward();
        //*
        int startIndex = 0;
        int firstImage = 0;
        for(int index=0; index<10; index++) {
            startIndex = 0;
            while (fromOneHotToNumber(trainLabels[startIndex]) != index) {
                startIndex++;
            }
            firstImage = startIndex;
            startIndex++;
            for(int num=0; num<20; num++) {
                //int firstImage = (int) (Math.random()*trainInputs.length);
                while (fromOneHotToNumber(trainLabels[startIndex]) != index) {
                    startIndex++;
                }
                int secondImage = startIndex;
                startIndex++;
                float[] differenceVector = new float[resizedInput[0].length];
                float[] inputSample = resizedInput[firstImage];
                for (int i = 0; i < differenceVector.length; i++) {
                    differenceVector[i] = resizedInput[secondImage][i] - inputSample[i];
                }

                network.setInput(inputSample);
                network.feedForward();
                image = graphics.drawNetwork();
                image = ImageUtils.resize(image, sizeX, sizeY);
                images.add(image);
                ImageUtils.display(image);
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < differenceVector.length; j++) {
                        inputSample[j] = inputSample[j] + differenceVector[j] / 5;
                    }
                    network.setInput(inputSample);
                    network.feedForward();
                    image = graphics.drawNetwork();
                    image = ImageUtils.resize(image, sizeX, sizeY);
                    images.add(image);
                    ImageUtils.display(image);
                }
                firstImage = secondImage;
            }
        }
        //*/

        int k=0;
        boolean direction = true;
        long timer = System.currentTimeMillis();
        while(true)
        {
            while(timer + 100 > System.currentTimeMillis()){

            }
            timer = System.currentTimeMillis();
            if(k>=images.size()) {
                k=0;
            }
            ImageUtils.display(images.get(k));
            k++;
        }
        /*
        for (int i = 0; i < 25; i++)
        {
            network.setInput(resizedInput[i]);
            network.feedForward();
            image = graphics.drawNetwork();
            image = ImageUtils.resize(image, 1000, 670);
            ImageUtils.display(image);
        }
        */
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
        int size = 64;
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Arial Black", Font.BOLD, size));
        graphics.drawString(text, image.getWidth() - 800, 80);
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
            float[][] cropped2DProjection = new float[28][28];
            for(x=0; x<projection2D.length - 0; x++)
            {
                for(y=0; y<projection2D.length - 0; y++)
                {
                    cropped2DProjection[x-0][y-0] = projection2D[y][x];
                }
            }
            float[][] rescaled2DProjection = new float[14][14];
            for(x=0; x<rescaled2DProjection.length; x++)
            {
                for(y=0; y<rescaled2DProjection.length; y++)
                {
                    float pix1 = cropped2DProjection[x*2][y*2];
                    float pix2 = cropped2DProjection[x*2+1][y*2];
                    float pix3 = cropped2DProjection[x*2][y*2+1];
                    float pix4 = cropped2DProjection[x*2+1][y*2+1];
                    float avg = (pix1 + pix2 + pix3 + pix4)/4f;
                    rescaled2DProjection[x][y] = (1-avg);
                }
            }
            float[] rescaled1DProjection = new float[rescaled2DProjection.length*rescaled2DProjection.length];
            int k=0;
            for(x=0; x<rescaled2DProjection.length; x++)
            {
                for(y=0; y<rescaled2DProjection.length; y++)
                {
                    rescaled1DProjection[k] = rescaled2DProjection[y][x];
                    k++;
                }
            }
            returnValue[i] = rescaled1DProjection;
        }
        return returnValue;
    }
}
