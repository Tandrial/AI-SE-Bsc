#ifndef ASSERT_H
  #define ASSERT_H

  #ifndef DEBUG
    #define ASSERT(n)
  #else
    #include <stdio.h>
    #include <stdlib.h>
    #define ASSERT(n) \
      if(!(n)) { \
        printf("%s - Failed ", #n); \
        printf("On %s ", __DATE__); \
        printf("At %s ", __TIME__); \
        printf("In File %s ", __FILE__); \
        printf("At Line %d\n", __LINE__); \
        exit(1);}
  #endif
#endif