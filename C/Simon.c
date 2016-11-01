/*
* Author: Cinnie Hsiung
* Student Number: 12859147
* Lab Section: L1G
* Date: November 27 2014
* Purpose: Plays the Simon Game
*/

#include <stdio.h>
#include <stdlib.h>
#include <DAQlib.h>
#include <Windows.h>
#include <time.h>

#define TRUE 1
#define FALSE 0
#define ON 1
#define OFF 0

#define SIZE 5
#define GREEN 0
#define RED 1
#define YELLOW 2
#define BLUE 3
#define NONE 4

/*function protoypes*/
void runSimon(void);
void getSequence(int Sequence[]);
void flashLED(int channelNumber, int time);
int readAllbuttons(void);
void evalSignal(int winCondition);
void flashSequence(int stopNum, int Sequence[]);
int checkSequence(int stopNum, int Sequence[]);
void newGameSignal(void);

/*main function*/
int main(void){
	int setupNum;
	srand((unsigned) time(NULL));

	printf("Enter the setup number for DAQ: ");
	scanf("%d", &setupNum);

	if(setupDAQ(setupNum)==TRUE)
		runSimon();
	else
		printf("ERROR: Cannot initialize DAQ\n");

	system("PAUSE");
	return 0;
}

/*
* Function: runSimon
* Executes the Simon Game
*/
void runSimon(void){
	int stopNum;
	int winCondition;
	int buttonState;
	int simonSequence[SIZE];

	while(continueSuperLoop()==TRUE){
		getSequence(simonSequence);
		stopNum = 1;
		winCondition = TRUE;
		buttonState = NONE;

		newGameSignal();
		do{
			flashSequence(stopNum, simonSequence);
			winCondition = checkSequence(stopNum, simonSequence);

			stopNum++;
		} while(stopNum <= SIZE && winCondition == TRUE);

		evalSignal(winCondition);
		Sleep(500);
	}
}

/*
* Function: getSequence
* Generates a random sequence of numbers from 0 to 3 (the channel numbers of the LEDs available)
* of length SIZE
* Parameter: Sequence - the array in which the sequence of numbers will be stored
*/
void getSequence(int Sequence[]){
	int index;
	for(index = 0; index < SIZE; index++){
		Sequence[index] = (rand() % 4);
	}
}

/*
* Function: flashLED
* Flashes a specified LED for a specified time
* Parameter: channelNumber - the channel number of the LED to flash
* Parameter: time - the length of time in milliseconds to flash the LED
*/
void flashLED(int channelNumber, int time){
	digitalWrite(channelNumber, ON);
	Sleep(time);
	digitalWrite(channelNumber, OFF);
	Sleep(time);
}

/*
* Function: flashSequence
* Flashes the designated length of a sequence of LEDs for 0.25 seconds each
* Parameter: stopNum - the length of the sequence
* Parameter: Sequence - the array that contains the sequence of LEDs
*/
void flashSequence(int stopNum, int Sequence[]){
	int count;
	Sleep(700);
	for(count = 0; count < stopNum; count++){
		flashLED(Sequence[count], 250);
	}
}

/*
* Function: checkSequence
* Checks the player's sequence of buttons to the generated sequence of flashes
* Parameter: stopNum - the length of the sequence
* Parameter: Sequence - the array in which the original sequence is stored
* Returns: TRUE if the player is correct, FALSE if the player made a mistake
*/
int checkSequence(int stopNum, int Sequence[]){
	int count;
	int buttonState;

	for(count = 0; count < stopNum; count++){
		do{
			buttonState = readAllbuttons();
		} while(buttonState == NONE && continueSuperLoop()==TRUE);

		if(buttonState != Sequence[count])
			return FALSE;
	}
	return TRUE;
}

/*
* Function: readAllbuttons
* Reads the state of all four buttons whether the player clicks the button briefly or holds
* the button for an extended period of time. Flashes the corresponding LED of the button.
* Returns: the channel number of a button that is ON (0 to 3), or 4 if all buttons are OFF
*/
int readAllbuttons(void){
	int buttonstate = 4;
	if(digitalRead(GREEN) == ON){
		while(digitalRead(GREEN) == ON){}
		buttonstate = GREEN;
	}
	else if(digitalRead(RED) == ON){
		while(digitalRead(RED) == ON){}
		buttonstate = RED;
	}
	else if(digitalRead(YELLOW) == ON){
		while(digitalRead(YELLOW) == ON){}
		buttonstate = YELLOW;
	}
	else if (digitalRead(BLUE) == ON){
		while(digitalRead(BLUE) == ON){}
		buttonstate = BLUE;
	}
	if (buttonstate != NONE)
		flashLED(buttonstate, 250);
	return buttonstate;
}

/*
* Function: evalSignal
* Flashes the red light for 1.5 seconds if the player lost.
* Flashes all the LEDs in a wave motion if the player won.
* Parameter: winCondition - TRUE if the player won, FALSE if the player lost
*/
void evalSignal(int winCondition){
	if(winCondition == FALSE){
		Sleep(250);
		flashLED(RED, 1500);
	}
	else{
		int index;
		static int YAY[] = {GREEN, RED, YELLOW, BLUE, YELLOW, RED, GREEN};
	
		for(index = 0; index < 7; index++){
			flashLED(YAY[index], 150);
		}
	}
}

/*
* Function: newGameSignal
* Flashes all the LEDs at once to signal that a new game has begun
*/
void newGameSignal(void){
	int LEDs;
	Sleep(250);

	for(LEDs = 0; LEDs < 4; LEDs++){
		digitalWrite(LEDs, ON);
	}
	Sleep(1500);

	for(LEDs = 0; LEDs < 4; LEDs++){
		digitalWrite(LEDs, OFF);
	}
}