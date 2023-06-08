import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {SchemeMembership, UsersEntity} from "../../../../core/store/data/std/std.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {UserEntityService} from "../../../../core/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";
import {Router} from "@angular/router";
import {HttpErrorResponse} from "@angular/common/http";
import {QaService} from "../../../../core/store/data/qa/qa.service";

@Component({
    selector: 'app-review-standard-publication',
    templateUrl: './review-standard-publication.component.html',
    styleUrls: ['./review-standard-publication.component.css']
})
export class ReviewStandardPublicationComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();
    public tcSecs !: UsersEntity[];

    loading = false;
    loadingText: string;
    p = 1;
    p2 = 1;
    public tcTasks: SchemeMembership[] = [];
    public actionRequest: SchemeMembership | undefined;
    stdHOFReview: FormGroup;
    selectedNwi: string;
    public itemId: string = "";

    id: any = "Ongoing Applications";
    displayUsers: boolean = false;

    constructor(
        private schemeMembershipService: SchemeMembershipService,
        private service: UserEntityService,
        private standardDevelopmentService: StandardDevelopmentService,
        private SpinnerService: NgxSpinnerService,
        private notifyService: NotificationService,
        private qaService: QaService,
        private router: Router,
        private formBuilder: FormBuilder
    ) {
    }

    ngOnInit(): void {
        this.getApplicationsForReview();
        this.stdHOFReview = this.formBuilder.group({
            sicAssignedId: ['', Validators.required],
            requestId: ['', Validators.required],

        });
    }

    public getApplicationsForReview(): void {
        this.loading = true;
        this.SpinnerService.show()
        this.schemeMembershipService.getHodTasksUnassigned().subscribe(
            (response: SchemeMembership[]) => {
                console.log(response);
                this.tcTasks = response;
                this.rerender()
                this.SpinnerService.hide()
                this.displayUsers = true;

                this.loading = false;

            },
            (error: HttpErrorResponse) => {
                alert(error.message);
                this.SpinnerService.hide()
                this.displayUsers = true;
                this.loading = false;
            }
        )
    }

    // public onOpenModal(task: SchemeMembership, mode: string): void {
    public onOpenModal(mode: string): void {

        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        // console.log(task.requestId)

        // this.itemId = String(task.requestId);
        if (mode === 'edit') {
            this.stdHOFReview.reset();
            this.getTcSecs()

            // this.actionRequest = task;
            // this.selectedNwi = String(this.actionRequest.requestId)
            // this.stdHOFReview.controls.requestId.setValue(this.selectedNwi);

            button.setAttribute('data-target', '#voteDecisionModal');
        }
        container.appendChild(button);
        button.click();

    }

    @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
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

    ngOnDestroy(): void {
        // Do not forget to unsubscribe the event
        this.dtTrigger1.unsubscribe();


    }

    public onReviewTask(): void {

        this.qaService.showSuccess("Successfully Assigned")
        this.hideModel()
        this.stdHOFReview.reset();


        // if (this.stdHOFReview.valid) {
        //   this.SpinnerService.show();
        //
        //   this.schemeMembershipService.assignTask(this.stdHOFReview.value).subscribe(
        //       (response) => {
        //         this.showToasterSuccess(response.httpStatus, `SIC Assigned`);
        //
        //         this.SpinnerService.hide();
        //
        //         this.getApplicationsForReview();
        //         this.stdHOFReview.reset();
        //         this.hideModel()
        //       },
        //       (error: HttpErrorResponse) => {
        //         alert(error.message);
        //         this.SpinnerService.hide();
        //
        //       }
        //   )
        // } else {
        //   this.showToasterError("Error", `Please Fill In All The Fields.`);
        //
        // }
    }

    showToasterError(title: string, message: string) {
        this.notifyService.showError(message, title)

    }

    showToasterSuccess(title: string, message: string) {
        this.notifyService.showSuccess(message, title)

    }

    public getTcSecs(): void {
        this.standardDevelopmentService.getTcSec().subscribe(
            (response: UsersEntity[]) => {
                this.tcSecs = response;
            },
            (error: HttpErrorResponse) => {
                alert(error.message);
            }
        );
    }


    tabChange(ids: any) {
        this.id = ids;
        if (this.id == "Ongoing Applications") {
            this.reloadCurrentRoute()
        }

    }

    reloadCurrentRoute() {
        let currentUrl = this.router.url;
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
            this.router.navigate([currentUrl]);
        });
    }

}
