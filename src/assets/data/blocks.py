from pprint import pprint
file = open("block_names.txt", 'r').read().strip().split("\n")
blocks = {}
ids = [""*i for i in range(len(file))]
for line in file:
	n, i = line.split(" = ")
	i = int(i)
	blocks[n] = i
	ids[i] = n

while True:
	user = int(input("Enter id number (-1 to exit): "))
	if user == -1:
		break
	else:
		if (0 <= user < len(ids)):
			print(ids[user])
		else:
			print("THATS NOT A VALID BLOCK")
