#include<stdio.h>
#include<conio.h>
#include<string.h>
#include<ctype.h>
#include<stdlib.h>

struct node
{
	int isWord;
	struct node *children[27];
};

int getIndex(char);
struct node *getNode();
int load(struct node*);
void check(struct node*,const char*);

int main()
{
	int res,i=0;
	char st[100],c,fname[100];
	FILE *fp;
	struct node *root=getNode();
    res=load(root);
    printf("Welcome to the spell checker program\n");
	printf("\nEnter the name of the file you want to scan:");
	scanf("%s",fname);
	fp=fopen(fname,"r");
	if(fp==NULL)
		printf("\nCould not open file. Please enter the correct filename.");
	else
	{
		printf("\nThe words spelt incorrectly in the file are:");
    	for(c=fgetc(fp);c!=EOF;c=fgetc(fp))
    	{
    		c=tolower(c);
			if(c=='\n' || c==' ')
    		{
				st[i]='\0';
				check(root,st);
    			i=0;
			}
			else if(isdigit(c) || ispunct(c))
			{
				continue;
			}
			else
			{
				st[i]=c;
				i++;
			}
		}
	}
    
    fclose(fp);
    getch();
	return 0;
}

int getIndex(char c)
{
	if(c=='\'')
	   return 26;
	
	else
	  return (c-'a');
}

struct node *getNode() 
{ 
    struct node *par = NULL; 
  
    par = (struct node*)malloc(sizeof(struct node)); 
  
    if (par) 
    { 
        int i; 
  
        par->isWord = 0; 
  
        for (i = 0; i < 26; i++) 
            par->children[i] = NULL; 
    } 
  
    return par; 
} 
  

int load(struct node *root)
{
	int index;
	int c;
	struct node *cur=root;
    
	FILE *fp1=fopen("dictionary.txt","r");
	if(fp1==NULL)
	{
		printf("Could not open file\n");
		return 0;
	}
	for(c=fgetc(fp1);c!=EOF;c=fgetc(fp1))
	{
		if(c=='\n')
		{
			cur->isWord=1;
			cur=root;
		}
	    else
	    {
			index=getIndex(c);
			if(!cur->children[index])
			  cur->children[index]=getNode();
			  
			cur=cur->children[index];
	    }
    }
		
		fclose(fp1);
	return 1;
}

void check(struct node *root,const char *word)
{
	struct node *cur;
	cur=root;
	int i,index;
	for(i=0;word[i]!='\0';i++)
	{
		index=getIndex(word[i]);
		if(cur->children[index]==NULL)
		{
			printf("\n%s",word);
			return;
		}
		
		cur=cur->children[index];
	}
	return;
}

