
create table Users(
	ID varchar(10) primary key,
	name varchar(100),
	cName varchar(50)
);

create table Follows(
	ID1 varchar(10),
	ID2 varchar(10),
	primary key (ID1, ID2),
	check (ID1 != ID2),
	foreign key (ID1) references Users(ID) on delete cascade,
	foreign key (ID2) references Users(ID)
);

create table Interactions(
	authorID varchar(10),
    cNum int,
	uID varchar(10),
    iType varchar(8) check (iType in ('like', 'follow', 'comment', 'share')),
    primary key (authorID, cNum, uID, iType),
    foreign key (authorID) references Users(ID) on delete cascade,
	foreign key (uID) references Users(ID)
);
