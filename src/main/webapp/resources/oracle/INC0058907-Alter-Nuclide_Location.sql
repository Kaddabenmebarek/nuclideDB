

drop TABLE nuclide_lab_isotope;
drop SEQUENCE nuclide_lab_isotope_sequence;

update nuclide_location set location_type = 'clab' where location = 'Waste room H91.U2.N03';

commit;