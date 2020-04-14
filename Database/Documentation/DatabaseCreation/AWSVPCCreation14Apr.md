Creating an Amazon VPC (Console)
To create an ElastiCache cluster inside an Amazon Virtual Private Cloud

1. Sign in to the AWS Management Console, and open the Amazon VPC console at https://console.aws.amazon.com/vpc/.

2. Create a new Amazon VPC by using the Amazon Virtual Private Cloud wizard:

	a. In the navigation list, choose VPC Dashboard.

	b. Choose Start VPC Wizard.

	c. In the Amazon VPC wizard, choose VPC with Public and Private Subnets, and then choose Next.

	d. On the VPC with Public and Private Subnets page, keep the default options, and then choose Create VPC.

	e. In the confirmation message that appears, choose Close.

3. Confirm that there are two subnets in your Amazon VPC, a public subnet and a private subnet. These subnets are created automatically.

	a. In the navigation list, choose Subnets.

	b. In the list of subnets, find the two subnets that are in your Amazon VPC:
	![test image](/Database/Documentation/DocumentationImages/vpc-01.png)


	The public subnet will have one fewer available IP address, because the wizard creates an Amazon EC2 NAT instance and an Elastic IP address (for which Amazon EC2 rates apply) for outbound communication to the Internet from your private subnet.

Tip
Make a note of your two subnet identifiers, and which is public and private. You will need this information later when you launch your cache clusters and add an Amazon EC2 instance to your Amazon VPC.

4. Create an Amazon VPC security group. You will use this group for your cache cluster and your Amazon EC2 instance.

	a. In the navigation pane of the Amazon VPC Management console, choose Security Groups.

	b. Choose Create Security Group.

	c. Type a name and a description for your security group in the corresponding boxes. In the VPC box, choose the identifier for your Amazon VPC.


			Image: Create Security Group screen
		![test image2](/Database/Documentation/DocumentationImages/vpc-02.png)
								
	d. When the settings are as you want them, choose Yes, Create.

5. Define a network ingress rule for your security group. This rule will allow you to connect to your Amazon EC2 instance using Secure Shell (SSH).

	a. In the navigation list, choose Security Groups.

	b. Find your security group in the list, and then choose it.

	c. Under Security Group, choose the Inbound tab. In the Create a new rule box, choose SSH, and then choose Add Rule.

	d. Choose Apply Rule Changes.

Now you are ready to create a cache subnet group and launch a cache cluster in your Amazon VPC.

Link from AWS: https://docs.aws.amazon.com/AmazonElastiCache/latest/mem-ug/VPCs.CreatingVPC.html
