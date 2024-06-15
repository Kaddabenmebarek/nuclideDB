insert into nuclide values('125I', 60.14, 0.7);
insert into nuclide values('32P', 14.29, 4.0);
insert into nuclide values('3H,HTO', 4508, 600);
insert into nuclide values('3H', 4508, 200);
insert into nuclide values('14C', 2091450, 20);
insert into nuclide values('35S', 87.44, 10);

insert into nuclide_user values('BOUKHAC','Celine', 'Boukhadra'); 
insert into nuclide_user values('REINJ', 'Josiane', 'Rein');
insert into nuclide_user values('SIPPELV', 'Virginie', 'Sippel');
insert into nuclide_user values('HAENIGB', 'Benedicte', 'Haenig');
insert into nuclide_user values('MENYHAK', 'Katalin', 'Menyhart');
insert into nuclide_user values('MUELLEC', 'Celia', 'Mueller');
insert into nuclide_user values('STUDERR', 'Studer', 'Rolf');
insert into nuclide_user values('DELAHAS', 'Stephan', 'Delahaye');
insert into nuclide_user values('SCHNEIR', 'Ralph', 'Schneiter');

GRANT nuclide_user to BOUKHAC;
GRANT nuclide_user to REINJ;
GRANT nuclide_user to SIPPELV;
GRANT nuclide_user to HAENIGB;
GRANT nuclide_user to MENYHAK;
GRANT nuclide_user to MUELLEC;
GRANT nuclide_user to STUDERR;
GRANT nuclide_user to DELAHAS;
GRANT nuclide_user to SCHNEIR;