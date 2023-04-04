# CQRS Pattern and Load Test
Credits: https://gortonator.github.io/bsds-6650/assignments-2022/Assignment-4

## Requirements 
Step 1: Redesign the application to implement CQRS pattern

Step 2: Populate the database with 500k requests. 

Step 3: Load test the system
*Create 3 thread groups, one for each API you have built (ie 2 GETs and 1 POST)
*The GET thread groups should each have 128 threads, execute 500 iterations and have a 10 second ramp up time
*The POST thread group should have 64 threads, execute 250 iterations and have a 10 second ramp up time
*Randomly generate a swipe event data/requests as per assignment 3 (or whatever makes sense!)
