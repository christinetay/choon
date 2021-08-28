//
// Created by Tay on 16/4/2018.
//

#include "opencv2/opencv.hpp"
#include <opencv2/core/core.hpp>
#include <iostream>
#include <jni.h>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;
int negOne =-1;

JNIEXPORT void JNICALL Java_com_opencv_businesscamera_opencvc1_MainActivity_loadImage(JNIEnv *env, jclass clazz,ifstream file)
        {
////                if(argc !=2)
////                {
////                        cout <<"Usage:display_image ImageToLoadAndDisplay"<<endl;
////                        //return -1;
////                }
//
//                Mat image;
//                std:: string info("");
//                file >> info;
//                image = imread(info, CV_LOAD_IMAGE_COLOR);  //read the file
//
//                if(! image.data)                                //check for invalid input
//                {
//                        cout<<"Could not open or find the image" << std::endl;
//
////                        return negOne;
//                }
//
//                namedWindow("Display Window",WINDOW_AUTOSIZE); //create window for display
//                imshow("Display window",image);
//
//                waitKey(0);

        }

