select t1.Id_zakazky, t1.dilci_termin -- , count(*) as 'počet datumu přede mnou včetně mě'
from dilci_terminy as t1 join dilci_terminy as t2 
	on (t1.Id_zakazky = t2.Id_zakazky and t1.dilci_termin >= t2.dilci_termin)
where t1.splnen = false and t2.splnen = false
group by Id_zakazky, dilci_termin
having count(*) <=3
order by t1.Id_zakazky, t1.dilci_termin;