

#include "pch.h"
#include <iostream>
#include <mpi.h>
using namespace std;

int main(int argc, char** argv)
{
	int world_size, world_rank;
	MPI_Status status;
	int* pMyPart=0;
	int array_len = 15;
	int sum = 0;
	int * sum_array=0;
	int *pArray = 0;
	int i;

	MPI_Init(&argc, &argv);
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);
	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	if (world_size != 3)
	{
		printf("This application is meant to be run with 3 processes.\n");
		MPI_Abort(MPI_COMM_WORLD, EXIT_FAILURE);
	}
	
	pMyPart = (int *)malloc(sizeof(int) * array_len / world_size);

	if (world_rank == 0)
	{
		srand(world_size);
		pArray = (int *)malloc(sizeof(int) * array_len);

		for (i = 0; i < array_len; i++) pArray[i] = rand() % 10;

		printf(" generated array is : ");
		for (i = 0; i < array_len; i++) printf(" %d   \t",pArray[i]);
		printf("\n \n ");

	}

	MPI_Scatter(pArray, array_len / world_size, MPI_INT, pMyPart, array_len / world_size, MPI_INT, 0, MPI_COMM_WORLD);

	printf(" process(%d):  ", world_rank);
	for (i = 0; i < array_len / world_size; i++)
	{
		sum += pMyPart[i];
		printf("%d   \t", pMyPart[i]);
		}
	printf(" my sum is %d ", sum);
	if (world_rank==0) sum_array = (int *)malloc(sizeof(int) *  world_size);


	MPI_Gather(&sum, 1, MPI_INT, sum_array, 1, MPI_INT, 0, MPI_COMM_WORLD);

	if (world_rank == 0)
	{

		int final_sum=0;
		for (int k = 0; k < world_size; k++) final_sum += sum_array[k];

		printf(" Summation of the generated array  is %d", final_sum);

	}

	MPI_Finalize();

	return EXIT_SUCCESS;
}

/*

int  arr[15] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	int parr[3] = { 0,0,0 };
	//printf("process  %d : summation of my part is %d\n", world_rank, parr[world_rank]);

	if (world_rank == 0) {



		MPI_Request request;
		MPI_Isend(&arr, sizeof(arr), MPI_INT,1, 0, MPI_COMM_WORLD,&request);

		MPI_Wait(&request, MPI_STATUS_IGNORE);
		MPI_Isend(&arr, sizeof(arr), MPI_INT, 2, 0, MPI_COMM_WORLD,&request);

		MPI_Wait(&request, MPI_STATUS_IGNORE);
		for (int i= world_rank*5; i < (world_rank+1)*5; i++)	parr[world_rank] += arr[i];



	}
	else
	{
		MPI_Request request;
		MPI_Recv(&arr, sizeof(arr), MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		for (int i = world_rank * 5; i < (world_rank + 1) * 5; i++)	parr[world_rank] += arr[i];

		MPI_Isend(&parr[world_rank], 1, MPI_INT, 0, 0, MPI_COMM_WORLD,  &request);
		MPI_Wait(&request, MPI_STATUS_IGNORE);
		printf("process  %d : summation of my part is %d\n", world_rank, parr[world_rank]);
	}



	// Now Master can receive from the last process.
	if (world_rank == Master) {
		MPI_Recv(&parr[1], 1, MPI_INT, 1, 0,MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		MPI_Recv(&parr[2], 1, MPI_INT, 2, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		printf(" final summation is %d ",parr[0]+parr[1]+parr[2]);
	}

*/