import {Component, OnInit, QueryList, ViewChildren} from '@angular/core';
import {FormationOfTcService} from "../../../../core/store/data/std/formation-of-tc.service";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {NgxSpinnerService} from "ngx-spinner";
import {FormBuilder} from "@angular/forms";
import {MasterService} from "../../../../core/store/data/master/master.service";
import {ActivatedRoute} from "@angular/router";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {JustificationForTc} from "../../../../core/store/data/std/formation_of_tc.model";
import {HttpErrorResponse} from "@angular/common/http";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";

@Component({
    selector: 'app-all-proposals',
    templateUrl: './all-proposals.component.html',
    styleUrls: ['./all-proposals.component.css']
})
export class AllProposalsComponent implements OnInit {

  approvedJustifications: JustificationForTc[] = [];
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger: Subject<any> = new Subject<any>();
  dtTrigger4: Subject<any> = new Subject<any>();

  constructor(private formationOfTcService: FormationOfTcService,
                private notifyService: NotificationService,
                private SpinnerService: NgxSpinnerService,
                private formBuilder: FormBuilder,
                private masterService: MasterService,
                private route: ActivatedRoute,
                private standardDevelopmentService: StandardDevelopmentService,
    ) {
    }

    ngOnInit(): void {
        this.getAllSacJustificationsForWebsite()

    }

  public getAllSacJustificationsForWebsite(): void {
    this.SpinnerService.show()
    this.formationOfTcService.sacGetAllForWebsite().subscribe(
        (response: JustificationForTc[]) => {
          this.approvedJustifications = response;
          this.rerender()
          this.SpinnerService.hide()

        },
        (error: HttpErrorResponse) => {
          alert(error.message);
          this.SpinnerService.hide()

        }
    );
  }


  rerender(): void {
    this.dtElements.forEach((dtElement: DataTableDirective) => {
      if (dtElement.dtInstance)
        dtElement.dtInstance.then((dtInstance: DataTables.Api) => {
          dtInstance.destroy();
        });
    });
    setTimeout(() => {
      this.dtTrigger.next();
      this.dtTrigger4.next();


    });

  }
  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event

    this.dtTrigger4.unsubscribe();
    this.dtTrigger.unsubscribe();

  }




}
