#include <stdio.h>

#define SIZE_X 3
#define SIZE_Y 2
#define SIZE_Z 4

int cube[SIZE_X][SIZE_Y][SIZE_Z] = {{ {1, 2,  3, 4  }/*, {0, 0, 0,    0  }*/},
                                    { {2, 3/*,0, 0*/}  , {4, 6, 8,   10  }  },
                                    { {3, 4,  5, 6  }  , {6, 8, 10/*, 0*/}  }};

int main(void) {

	int x = 1;
	int y = 1;
	int z = 2;
	// p ist ein Pointer auf einen int
	int *p = &cube[0][0][0];	
	// vptr ist ein Pointer auf ein 1D Array der Größe SIZE_Z
	int (*vptr)[SIZE_Z] = &cube[x][y];
	// mptr ist ein Pointer auf ein 2D Array mit den Dimensionen SIZE_Y * SIZE_Z
	int (*mptr)[SIZE_Y][SIZE_Z] = &cube[x];	

	//Speicher Layout eines 3D Array	
	printf("Adr cube[0][0][0] = %p\n", &cube[0][0][0]);
	printf("Adr cube[0][0][1] = %p\n", &cube[0][0][1]);
	printf("Adr cube[0][1][0] = %p\n", &cube[0][1][0]);
	printf("Adr cube[0][1][1] = %p\n", &cube[0][1][1]);
	printf("Adr cube[1][0][0] = %p\n", &cube[1][0][0]);
	printf("Adr cube[1][1][0] = %p\n", &cube[1][1][0]);
	printf("Adr cube[1][1][2] = %p\n", &cube[1][1][2]);
	printf("\n\n");	
	//Start
	printf("Adr cube[0][0][0] = %p\n", &p);
	// Start des 1D Array
	printf("Adr cube[%d][%d]    = %p\n", x, y, &vptr);
	// Start des 2D Array
	printf("Adr cube[%d]       = %p\n", x, &mptr);
	printf("\n\n");
	
	printf("x = %d, y = %d, z = %d\n", x, y, z);
	printf("cube[x][y][z]       = %d\n", cube[x][y][z]);

	// Da Cube als 1D-Array im Speicher dargestellt ist, kann der Pointer p über
	// die Indexe x, y, z an die korrekte Stelle verschoben werden	(siehe Report)<<
	printf("cube[x][y][z] *p    = %d\n", *(p + SIZE_Y * SIZE_Z * x + SIZE_Z * y + z));	

	// *vptr zeigt auf das 1. Element in einem 1D-Array und kann über z an die 
	// korrekte Position geschoben werden	
	printf("cube[x][y][z] *vptr = %d\n", *(*vptr + z));

	// *mptr zeigt auf das 1. Element in einem 2D-Array und kann über y, z 
	// an die korrekte Position geschoben werden	
	printf("cube[x][y][z] *mptr = %d\n", *(*(*mptr + y) + z));
		
	return 0;
}
