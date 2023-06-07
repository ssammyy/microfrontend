import {Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {DataTableDirective} from "angular-datatables";
import {Subject} from "rxjs";
import {SchemeMembership} from "../../../../core/store/data/std/std.model";
import {HttpErrorResponse} from "@angular/common/http";
import {SchemeMembershipService} from "../../../../core/store/data/std/scheme-membership.service";
import {UserEntityService} from "../../../../core/store";
import {StandardDevelopmentService} from "../../../../core/store/data/std/standard-development.service";
import {NgxSpinnerService} from "ngx-spinner";
import {NotificationService} from "../../../../core/store/data/std/notification.service";

@Component({
    selector: 'app-scheme-member-ship-review-all-applications',
    templateUrl: './scheme-member-ship-review-all-applications.component.html',
    styleUrls: ['./scheme-member-ship-review-all-applications.component.css']
})
export class SchemeMemberShipReviewAllApplicationsComponent implements OnInit {

    dtOptions: DataTables.Settings = {};
    @ViewChildren(DataTableDirective)
    dtElements: QueryList<DataTableDirective>;
    dtTrigger1: Subject<any> = new Subject<any>();

    loading = false;
    loadingText: string;
    displayUsers: boolean = false;

    public tcTasks: SchemeMembership[] = [];
    public itemId: string = "";
    public actionRequest: SchemeMembership | undefined;


    constructor(private schemeMembershipService: SchemeMembershipService,
                private service: UserEntityService,
                private standardDevelopmentService: StandardDevelopmentService,
                private SpinnerService: NgxSpinnerService,
                private notifyService: NotificationService,
    ) {
    }

    ngOnInit(): void {
        this.getApplicationsForReview();

    }

    public getApplicationsForReview(): void {
        this.loading = true;
        this.SpinnerService.show()
        this.schemeMembershipService.getHodTasks().subscribe(
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

    public onOpenModal(task: SchemeMembership, mode: string): void {
        const container = document.getElementById('main-container');
        const button = document.createElement('button');
        button.type = 'button';
        button.style.display = 'none';
        button.setAttribute('data-toggle', 'modal');
        console.log(task.requestId)
        button.setAttribute('data-target', '#voteDecisionModal');
        this.actionRequest = task;
        this.itemId = String(task.requestId);

        container.appendChild(button);
        button.click();

    }
    @ViewChild('closeViewModal') private closeModal: ElementRef | undefined;

    public hideModel() {
        this.closeModal?.nativeElement.click();
    }


}
