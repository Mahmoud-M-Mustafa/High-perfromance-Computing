

#include "pch.h"
#include <iostream>
#include <mpi.h>
using namespace std;
#define Master  0
int main(int argc, char** argv)
{
	MPI_Init(&argc, &argv);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);

	int world_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &world_rank);
	int data;
	if (world_rank != Master) {

		MPI_Recv(&data, 1, MPI_INT, world_rank - 1, 0,MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		printf("Process %d received data %d from process %d\n",world_rank, data, world_rank - 1);
	}
	else
	{
		data = 0;
	}


	MPI_Send(&data, 1, MPI_INT, (world_rank + 1) % world_size,0, MPI_COMM_WORLD);

	// Now Master can receive from the last process.
	if (world_rank == Master) {
		MPI_Recv(&data, 1, MPI_INT, world_size - 1, 0,MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		printf("Process %d received data %d from process %d\n",world_rank, data, world_size - 1);
	}

	MPI_Finalize();

	return EXIT_SUCCESS;
}