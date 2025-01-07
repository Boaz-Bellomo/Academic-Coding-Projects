cmake_minimum_required(VERSION 3.10)

# Set the project name
project(HW3 C)

# Set C standard
set(CMAKE_C_STANDARD 11)
set(CMAKE_C_STANDARD_REQUIRED ON)

# Add math library
link_libraries(m)

# Add executable
add_executable(HW3
        filter.c
        helpers.c
        )

# Add include directories
include_directories(${CMAKE_SOURCE_DIR})

# Set compiler warnings
if(MSVC)
    target_compile_options(HW3 PRIVATE /W4)
else()
    target_compile_options(HW3 PRIVATE -Wall -Wextra -pedantic)
endif()

# Set output directories
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/bin)
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)
set(CMAKE_ARCHIVE_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)

# Debug configuration
set(CMAKE_BUILD_TYPE Debug)