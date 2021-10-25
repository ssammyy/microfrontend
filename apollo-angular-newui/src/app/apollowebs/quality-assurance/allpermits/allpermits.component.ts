import {Component, OnInit} from '@angular/core';
import {UserEntityDto, UserEntityService} from "../../../core/store";
import {Observable, Subject} from "rxjs";
import {Titles, TitlesService} from "../../../core/store/data/title";
import {FormGroup} from "@angular/forms";
import {Store} from "@ngrx/store";
import {HttpErrorResponse} from "@angular/common/http";
import swal from "sweetalert2";
import {Router} from "@angular/router";
import {QaService} from "../../../core/store/data/qa/qa.service";
import {PermitEntityDto} from "../../../core/store/data/qa/qa.model";

@Component({
    selector: 'app-allpermits',
    templateUrl: './allpermits.component.html',
    styleUrls: ['./allpermits.component.css']
})
export class AllpermitsComponent implements OnInit {
    dtOptions: DataTables.Settings = {};
    dtTrigger: Subject<any> = new Subject<any>();
    public allPermitData: PermitEntityDto[];
    tcTasks: PermitEntityDto[] = [];
    public static permitNumber: string | number | string[];
    permitNumberFinal: string
    user: UserEntityDto;
    title$: Observable<Titles[]>;
    stepOneForm: FormGroup = new FormGroup({});
    public static permitId: string | number | string[];
    constructor(private store$: Store<any>,
                private service: UserEntityService,
                private titleService: TitlesService,
                private qaService: QaService,
                private router: Router
    ) {
    }

    ngOnInit(): void {

        swal.fire({
            title: 'Enter Permit Number',
            html: '<div class="form-group">' + '<input id="input-field" placeholder="Permit Number" type="text" class="form-control" />' + '</div>',
            showCancelButton: true,
            customClass: {confirmButton: 'btn btn-success', cancelButton: 'btn btn-danger',},
            buttonsStyling: false
        }).then((result) => {
            if (result.value) {
                this.qaService.loadPermitMigratededList(<string>$(`#input-field`).val()).subscribe(
                    (response: PermitEntityDto[]) => {
                        console.log(response);
                        this.tcTasks = response;
                        this.dtTrigger.next();
                    },
                    (error: HttpErrorResponse) => {
                        swal.fire({
                            title: 'Cancelled',
                            text: 'This Permit Is Not Assigned To You.',
                            icon: 'error',
                            customClass: {confirmButton: "btn btn-info",},
                            buttonsStyling: false
                        }).then((result) => {
                            if (result.value) {
                                window.location.href = "/dashboard";
                            }
                        });
                    }
                );


                swal.fire({
                    icon: 'success',
                    html: 'Permit id: <strong>' + $('#input-field').val() + ' is being migrated</strong>',
                    customClass: {confirmButton: 'btn btn-success',},
                    buttonsStyling: false
                }).then((result) => {

                });
            } else {
                swal.fire({
                    title: 'Cancelled',
                    text: 'You Did Not Enter A Permit Number :)',
                    icon: 'error',
                    customClass: {confirmButton: "btn btn-info",},
                    buttonsStyling: false
                }).then((result) => {
                    if (result.value) {
                        window.location.href = "/dashboard";
                    }
                });
            }
        });
    }
    public gotoHome() {
        this.router.navigate(['/home']);  // define your component where you want to go

    }


}