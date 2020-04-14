How to create EC2 Web server

Create a Key Pair
AWS uses public-key cryptography to secure the login information for your instance. A Linux instance has no password; you use a key pair to log in to your instance securely. You specify the name of the key pair when you launch your instance, then provide the private key when you log in using SSH.

If you haven't created a key pair already, you can create one using the Amazon EC2 console. Note that if you plan to launch instances in multiple Regions, you'll need to create a key pair in each Region. For more information about Regions, see Regions, Availability Zones, and Local Zones.

To create a key pair

1. Sign in to the AWS Management Console and open the Amazon EC2 console at https://console.aws.amazon.com/ec2/.

2. From the navigation bar, select a Region for the key pair. You can select any Region that's available to you, regardless of your location. However, key pairs are specific to a region; for example, if you plan to launch an instance in the US East (Ohio) Region, you must create a key pair for the instance in the US East (Ohio) Region.

![test image](/Database/Documentation/DocumentationImages/EC2_select_region.png) Select a region
					
3. In the navigation pane, under NETWORK & SECURITY, choose Key Pairs.

4. Choose Create key pair.

5. Do the following:

	a. For Name, enter a descriptive name for the new key pair, such as your name, followed by -key-pair, plus the Region name. For example, me-key-pair-useast2.

	b. For File format, choose the format in which to save the private key.

		-Choose pem to save the private key in a format that is used with OpenSSH.

		-Choose ppk to save the private key in a format that is used with PuTTY, a tool that enables you to connect to a Linux instance from Windows.

	c. Choose Create.

6. The private key file is automatically downloaded by your browser. The base file name is the name you specified as the name of your key pair, and the file name extension is .pem. Save the private key file in a safe place.

	Important
	This is the only chance for you to save the private key file. You'll need to provide the name of your key pair when you launch an instance and the corresponding private key each time you connect to the instance.

7. If you will use an SSH client on a Mac or Linux computer to connect to your Linux instance, use the following command to set the permissions of your private key file so that only you can read it.

chmod 400 your_user_name-key-pair-region_name.pem

If you do not set these permissions, then you cannot connect to your instance using this key pair. For more information, see Error: Unprotected Private Key File.

For more information, see Amazon EC2 key pairs.

Create a Security Group
Security groups act as a firewall for associated instances, controlling both inbound and outbound traffic at the instance level. You must add rules to a security group that enable you to connect to your instance from your IP address using SSH. You can also add rules that allow inbound and outbound HTTP and HTTPS access from anywhere.

Note that if you plan to launch instances in multiple Regions, you'll need to create a security group in each Region. For more information about Regions, see Regions, Availability Zones, and Local Zones.

Prerequisites

You'll need the public IPv4 address of your local computer. The security group editor in the Amazon EC2 console can automatically detect the public IPv4 address for you. Alternatively, you can use the search phrase "what is my IP address" in an Internet browser, or use the following service: Check IP. If you are connecting through an Internet service provider (ISP) or from behind a firewall without a static IP address, you need to find out the range of IP addresses used by client computers.

To create a security group with least privilege

1. Open the Amazon EC2 console at https://console.aws.amazon.com/ec2/.

	Tip
	Alternatively, you can use the Amazon VPC console to create a security group. However, the instructions in this procedure don't match the Amazon VPC console. Therefore, if you switched to the Amazon VPC console in the previous section, either switch back to the Amazon EC2 console and use these instructions, or use the instructions in Set Up a Security Group for Your VPC in the Amazon VPC Getting Started Guide.

2. From the navigation bar, select a Region for the security group. Security groups are specific to a Region, so you should select the same Region in which you created your key pair.


![test image](/Database/Documentation/DocumentationImages/EC2_select_region.png) Select a Region
					
3. Choose Security Groups in the navigation pane.

4. Choose Create Security Group.

5. Enter a name for the new security group and a description. Use a name that is easy for you to remember, such as your user name, followed by _SG_, plus the Region name. For example, me_SG_uswest2.

6. In the VPC list, select your default VPC for the Region.

7. On the Inbound tab, create the following rules (choose Add Rule for each new rule), and then choose Create:

	-Choose HTTP from the Type list, and make sure that Source is set to Anywhere (0.0.0.0/0).

	-Choose HTTPS from the Type list, and make sure that Source is set to Anywhere (0.0.0.0/0).

	-Choose SSH from the Type list. In the Source box, choose My IP to automatically populate the field with the public IPv4 address of your local computer. Alternatively, choose Custom and specify the public IPv4 address of your computer or network in CIDR notation. To specify an individual IP address in CIDR notation, add the routing suffix /32, for example, 203.0.113.25/32. If your company allocates addresses from a range, specify the entire range, such as 203.0.113.0/24.

	Warning
	For security reasons, we don't recommend that you allow SSH access from all IPv4 addresses (0.0.0.0/0) to your instance, except for testing purposes and only for a short time.

Tutorial from AWS link: https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/get-set-up-for-amazon-ec2.html#create-a-base-security-group
