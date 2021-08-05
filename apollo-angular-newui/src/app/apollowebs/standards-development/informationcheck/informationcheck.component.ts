import { Component, OnInit } from '@angular/core';
import {FeedbackEmail, InfoAvailableYes, Notifications} from "../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {NepPointService} from "../../../core/store/data/std/nep-point.service";
import {HttpErrorResponse} from "@angular/common/http";

declare interface DataTable {
    headerRow: string[];
    footerRow: string[];
    dataRows: string[][];
}

declare const $: any;

@Component({
  selector: 'app-informationcheck',
  templateUrl: './informationcheck.component.html',
  styleUrls: ['./informationcheck.component.css']
})
export class InformationcheckComponent implements OnInit {
    public dataTable: DataTable;
  public notifications: Notifications[] | undefined;
  public actionRequest: Notifications | undefined;
  public stdRequestFormGroup!: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private notificationService: NepPointService) { }

  ngOnInit(): void {
    //this.getRequests();
      let formattedArray = [];
      this.notificationService.sendGetRequest().subscribe(
          (data: any) => {
              this.notifications = data;
              // tslint:disable-next-line:max-line-length
              formattedArray = data.map(i => [i.taskData.requestDate, i.name, i.taskData.requesterName, i.taskData.requesterCountry,i.taskData.requesterInstitution,i.taskData.requesterEmail,i.taskData.requesterPhone,i.taskData.requesterSubject,i.taskData.requesterComment]);

              this.dataTable = {
                  headerRow: ['Date', 'Status', 'Requester Name', 'Country', ' Institution', 'Email','Phone','Subject','Comment','Actions'],
                  footerRow: ['Date', 'Status', 'Requester Name', 'Country', ' Institution', 'Email','Phone','Subject','Comment','Actions'],
                  dataRows: formattedArray


                  // ['REFFM#202107095913D', 'Andrew Mike', '09/07/2021', 'Dassani', 'Water', '']

              };

          }
      );
  }

  public getRequests(): void{
    this.notificationService.sendGetRequest().subscribe(
        (response: Notifications[]) => {
          this.notifications = response;
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public onReSubmit(notification: InfoAvailableYes): void{
    this.notificationService.reviewTasks(notification).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.getRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }


  public sendEmailFeedback(notification: FeedbackEmail): void{
    this.notificationService.feedbackEmail(notification).subscribe(
        (response: FeedbackEmail) => {
          console.log(response);
          this.getRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public onSubmit(notification: InfoAvailableYes): void{
    this.notificationService.reviewTasks(notification).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.getRequests();
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  public onOpenModal(notification: Notifications, mode: string): void{
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'yes'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#yesmodal');
    }
    if (mode === 'yes2'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#yesmodals');
    }
    if (mode === 'SendEmail'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#sendemail');
    }
    if (mode === 'no'){
      this.actionRequest = notification;
      button.setAttribute('data-target', '#noModal');
    }


    // @ts-ignore
    container.appendChild(button);
    button.click();

  }

  sendEnquiry(): void{

    this.notificationService.infoAvailableYes(this.stdRequestFormGroup.value).subscribe(
        (response: InfoAvailableYes) => {
          console.log(response);
          this.router.navigate(['/sendfeedback']);
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
    );
  }

  //datatable

    onSelect(rowElement: string) {
        this.router.navigate(['/invoiceDetails'], {fragment: rowElement});
    }

    ngAfterViewInit() {
        $('#datatables').DataTable({
            'pagingType': 'full_numbers',
            'lengthMenu': [
                [10, 25, 50, -1],
                [10, 25, 50, 'All']
            ],
            responsive: true,
            language: {
                search: '_INPUT_',
                searchPlaceholder: 'Search records',
            }

        });

        let table: any;
        table = $(`#datatables`).DataTable();

        // Edit record
        table.on('click', '.edit', function (e) {
            let $tr = $(this).closest('tr');
            if ($($tr).hasClass('child')) {
                $tr = $tr.prev('.parent');
            }

            let data: any;
            data = table.row($tr).data();
            alert('You press on Row: ' + data[0] + ' ' + data[1] + ' ' + data[2] + '\'s row.');
            e.preventDefault();
        });

        // Delete a record
        table.on('click', '.remove', function (e) {
            const $tr = $(this).closest('tr');
            table.row($tr).remove().draw();
            e.preventDefault();
        });

        // Like record
        table.on('click', '.like', function (e) {
            alert('You clicked on Like button');
            e.preventDefault();
        });

        $('.card .material-datatables label').addClass('form-group');
    }
}
