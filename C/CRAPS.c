/*
* Author: Cinnie Hsiung
* Date: October 13, 2014
* Purpose: Plays craps with the user
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define _CRT_SECURE_NO_WARNINGS
#define MAX_ROLL 6
#define MIN_BET 5
#define TRUE 1
#define FALSE 0

// function prototypes
int getWallet(void);
int makeBet(int);
int doAgain(void);
void goodbye(int);
int playRound(void);
int rollForPoint(int);
int rollDice(void);
int rollDie(void);

int main(void) {
	int wallet;
	int bet;
	srand((unsigned) time (NULL));
	wallet = getWallet();

	do {
		bet = makeBet(wallet);
		if (playRound()){
			wallet = wallet + bet;
			printf("You won! ");
		}
		else {
			wallet = wallet - bet;
			printf("You lost! ");
		}
		printf("You now have $%i in your wallet.\n\n", wallet);
	} while (doAgain() && wallet >= MIN_BET);

	goodbye(wallet);

	system("PAUSE");
	return 0;
}
	
/*
* prompts the user for the amount of money they have to play with
* in whole dollars
* rejets values under MIN_BET
* prompts until legitimate value is received
* returns the amounts of money entered by user
*/
int getWallet(void) {
	int wallet;
	printf("How lucky are you feeling? ");
	printf("Enter the amount you wish to play with: $");
	scanf("%d", &wallet);

	while (wallet < MIN_BET) {
		printf("That is not enough to play.");
		printf(" Please play with at least $%i.\n", MIN_BET);
		printf("How lucky are you feeling? ");
		printf("Enter the amount you wish to play with: $");
		scanf("%d", &wallet);
	}

	printf("You begin with $%i in your wallet.\n\n", wallet);

	return wallet;
}

/*
* prompts user to make a bet between MIN_BET and wallet
* promts until a legitiment value is entered
* parameter: wallet
* returns: user's bet
*/
int makeBet( int wallet ) {
	int bet;
	printf("The minimum bet is $%i. Make a bet: $", MIN_BET);
	scanf("%d", &bet);
	while ( bet < MIN_BET || bet > wallet ){
		printf("Your bet must be at least $%i and not greater than $%i.\n", MIN_BET, wallet);
		printf("Make a bet: $");
		scanf("%d", &bet);
	}

	printf("Your bet is $%d.\n\n", bet);
	return bet;
}

/*
* Ask user if they want to play again.
* returns true if they want to play again
* returns false otherwise
*/
int doAgain(void){
	int response;
	printf("Do you want to play again?\n");
	do {
		printf("If Yes, enter %i. If No, enter %i: ", TRUE, FALSE);
		scanf("%d", &response);
	} while( response != TRUE && response != FALSE );
	printf("\n");
	return response;
}

/*
* prints goodbye message
* if broke, tells them they have less than MIN_BET in their wallet
* otherwise tells them how much they have in wallet
* parameter: wallet
*/
void goodbye (int wallet){
	if ( wallet < MIN_BET ){
		printf("Sorry. You've gone broke. Bye now.\n");
	}
	else {
		printf("Well done.\n");
		printf("You still have $%i in your wallet. Goodbye. \n\n", wallet);
	}
}

/*
* rolls a single die
* returns: face value rolled
*/
int rollDie(void){
	int roll;

	roll = (rand() % MAX_ROLL) + 1;
	return roll;
}

/*
* rolls a pair of dice
* returns: sum of face values rolled
*/
int rollDice(void){
	int die1;
	int die2;
	int roll;

	die1 = rollDie();
	die2 = rollDie();

	roll = die1 + die2;
	printf("You rolled a %i.\n", roll);
	return roll;
}

/*
* plays a single round of craps with the user
* returns: true if user won round, false otherwise
*/
int playRound(void){
	int point;
	point = rollDice();

	if(point == 2 || point == 3 || point == 12){
		return FALSE;
	}

	else if(point == 7 || point == 11){
		return TRUE;
	}

	else {
		return rollForPoint(point);
	}
}

/*
* repeatedly rolls dice until either the point value or 7 is rolled
* parameter: point
* returns: true if user rolled point value before rolling a 7
* false otherwise
*/
int rollForPoint(int point){
	int wonRound;
	printf("Rolling for a %i.\n", point);
	do {
		wonRound = rollDice();
	} while (wonRound != 7 && wonRound != point);
	if (wonRound == point){
		return TRUE;
	}
	else {
		return FALSE;
	}
}
