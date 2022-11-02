import {Component, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {CompanyModel} from "../../../core/store/data/levy/levy.model";
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../core/store/data/std/notification.service";
import {LevyService} from "../../../core/store/data/levy/levy.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-standard-levy-registered-firms',
  templateUrl: './standard-levy-registered-firms.component.html',
  styleUrls: ['./standard-levy-registered-firms.component.css']
})
export class StandardLevyRegisteredFirmsComponent implements OnInit {

  companyModels: CompanyModel[]=[];
  dtOptions: DataTables.Settings = {};
  @ViewChildren(DataTableDirective)
  dtElements: QueryList<DataTableDirective>;
  dtTrigger1: Subject<any> = new Subject<any>();
  loadingText: string;
  blob: Blob;
  constructor(
      private SpinnerService: NgxSpinnerService,
      private notifyService : NotificationService,
      private levyService: LevyService,
  ) { }

  ngOnInit(): void {
    this.dtOptions = {
      processing: true,
      dom: 'Bfrtip'
    };
    this.getRegisteredFirms();
  }
  ngOnDestroy(): void {
    this.dtTrigger1.unsubscribe();
  }
  public getRegisteredFirms(): void{
    this.loadingText='Loading Registered Firms...';
    this.SpinnerService.show();
    this.levyService.getRegisteredFirms().subscribe(
        (response: CompanyModel[])=> {
          this.companyModels = response;
          this.SpinnerService.hide();
          this.rerender();
        },
        (error: HttpErrorResponse)=>{
          this.SpinnerService.hide();
          alert(error.message);
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
      this.dtTrigger1.next();
    });

  }

  levyRegisteredFirmsReport(id: number, fileName: string, applicationType: string): void {
    this.loadingText = "Generating Report ...."
    this.SpinnerService.show();
    this.levyService.levyRegisteredFirmsReport(id).subscribe(
        (dataPdf: any) => {
          this.SpinnerService.hide();
          this.blob = new Blob([dataPdf], {type: applicationType});
          let downloadURL = window.URL.createObjectURL(this.blob);
          const link = document.createElement('a');
          link.href = downloadURL;
          link.download = fileName;
          link.click();
        },
        error => {
          this.SpinnerService.hide();
          console.log(error);
          this.levyService.showError('AN ERROR OCCURRED');
        },
    );
  }

}
