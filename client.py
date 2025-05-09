import xmlrpc.client

proxy = xmlrpc.client.ServerProxy("http://localhost:8000/")
num = int(input("Enter an integer: "))
result = proxy.factorial(num)
print(f"Factorial of {num} is: {result}")
