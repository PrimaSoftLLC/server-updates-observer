#!/bin/sh


## Declare variables of the script
echo "INIT"
# Temp dirs for unpacked files of the application
target_app_dir="target_app"
target_lib_dir="target_lib"

result_dir="metr_results"

# Dir name for metrological files
metr_dir="$result_dir/metr"

temp_file="$result_dir/files_md5.txt"
out_file="$result_dir/out.txt"

# Variables for change console text color
RED="\033[0;31m"
NC="\033[0m"

echo "START"

# remove temp files and dirs if exists
rm -r -f "$target_app_dir"
rm -r -f "$target_lib_dir"
rm -r -f "$result_dir"

mkdir $result_dir

# Unpacking jar files of the application
unzip locator-*.jar -d target_app
unzip target_app/BOOT-INF/lib/distance-calculator* -d target_lib

# Create dir for metrological files of the application
mkdir $metr_dir

# Copy metrological files in a separate dir
cp target_app/BOOT-INF/classes/com/locator/server/domain/dto/mechanism/usagemode/UsageMode.class $metr_dir
cp target_app/BOOT-INF/classes/com/locator/server/domain/dto/mechanism/usagemode/UsageModeDiscrete.class $metr_dir
cp target_lib/by/nhorushko/distancecalculator/DistanceCalculatorImpl.class $metr_dir

# help for this script https://unix.stackexchange.com/questions/35832/how-do-i-get-the-md5-sum-of-a-directorys-contents-as-one-sum
# Calculate md5 sum for the all metrological files and write result in the temp file
find $metr_dir -type f -exec md5sum {} \; | sort -k 2 | tee $temp_file

# Remove directory from the file name
sed -i "s% $metr_dir/%%g" $temp_file

# copy results in the out file
cp $temp_file $out_file

# calculate total md5 sum for temp file that contains calculated md5 sums and file names
total_md5="$(md5sum $temp_file)"

# get only total md5 sum ignore file name
total_md5="${total_md5%% *}"

# write total md5 sum in the out file
echo "total: $total_md5" >> $out_file

# outputting the result to the console
echo -e "${RED}Md5 checksum for metrological part: ${NC}"
echo -e "${RED}$(cat $out_file)${NC}"


# Delete temp files and directories
rm -r "$target_app_dir"
rm -r "$target_lib_dir"

echo "END"
