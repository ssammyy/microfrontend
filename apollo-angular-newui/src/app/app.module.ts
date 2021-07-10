import {NgModule} from '@angular/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {APP_BASE_HREF, CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {MatNativeDateModule} from '@angular/material/core';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatRadioModule} from '@angular/material/radio';
import {MatSelectModule} from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import {MatSlideToggleModule} from '@angular/material/slide-toggle';
import {MatMenuModule} from '@angular/material/menu';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatListModule} from '@angular/material/list';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatStepperModule} from '@angular/material/stepper';
import {MatTabsModule} from '@angular/material/tabs';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatChipsModule} from '@angular/material/chips';
import {MatIconModule} from '@angular/material/icon';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatDialogModule} from '@angular/material/dialog';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {MatTableModule} from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';
import {MatPaginatorModule} from '@angular/material/paginator';

import {AppComponent} from './app.component';

import {SidebarModule} from './sidebar/sidebar.module';
import {FooterModule} from './shared/footer/footer.module';
import {NavbarModule} from './shared/navbar/navbar.module';
import {AdminLayoutComponent} from './layouts/admin/admin-layout.component';
import {AuthLayoutComponent} from './layouts/auth/auth-layout.component';

// import {routes} from './app.routing';
import {DashboardComponent} from './apollowebs/dashboard/dashboard.component';
import {InvoiceComponent} from './apollowebs/invoice/invoice.component';
import {AppRoutingModule} from './app.routing';
import {MdModule} from './md/md.module';
import {FmarkComponent} from './apollowebs/fmark/fmark.component';
import {DmarkComponent} from './apollowebs/dmark/dmark.component';
import {SmarkComponent} from './apollowebs/smark/smark.component';
import {FmarkallappsComponent} from './apollowebs/fmarkallapps/fmarkallapps.component';
import {St10FormComponent} from './apollowebs/st10-form/st10-form.component';
import {EffectsModule} from '@ngrx/effects';
import {EntityDataModule} from '@ngrx/data';
import {entityConfig} from './entity-metadata';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {NgxPaginationModule} from "ngx-pagination";
import {SharedModule} from "./core/shared/shared.module";
import {CoreModule} from "./core/core.module";
import {ToastrModule} from "ngx-toastr";
import {RegistrationComponent} from "./views/registration.component";
import {SignUpComponent} from "./views/registration/sign-up.component";
import {ResetCredentialsComponent} from "./views/registration/reset-credentials.component";
import {LoginComponent} from "./views/registration/login.component";
import {NewDmarkPermitComponent} from "./apollowebs/new-dmark-permit/new-dmark-permit.component";
import {PermitReportComponent} from "./apollowebs/permit-report/permit-report.component";
import { NewSmarkPermitComponent } from './apollowebs/new-smark-permit/new-smark-permit.component';

@NgModule({
    exports: [
        MatAutocompleteModule,
        MatButtonToggleModule,
        MatCardModule,
        MatChipsModule,
        MatCheckboxModule,
        MatStepperModule,
        MatDialogModule,
        MatExpansionModule,
        MatGridListModule,
        MatIconModule,
        MatInputModule,
        MatListModule,
        MatMenuModule,
        MatPaginatorModule,
        MatProgressBarModule,
        MatProgressSpinnerModule,
        MatRadioModule,
        MatSelectModule,
        MatDatepickerModule,
        MatButtonModule,
        MatSidenavModule,
        MatSliderModule,
        MatSlideToggleModule,
        MatSnackBarModule,
        MatSortModule,
        MatTableModule,
        MatTabsModule,
        MatToolbarModule,
        MatTooltipModule,
        MatNativeDateModule
    ],
    imports: [
        MdModule,
        CommonModule,
        EffectsModule.forRoot([]),
        EntityDataModule.forRoot(entityConfig),
        ReactiveFormsModule,
        RouterModule
    ],
    declarations: [
        LoginComponent,
        ResetCredentialsComponent,
        SignUpComponent,
        RegistrationComponent,
        DashboardComponent,
        InvoiceComponent,
        FmarkComponent,
        DmarkComponent,
        SmarkComponent,
        St10FormComponent,
        FmarkallappsComponent,
        NewDmarkPermitComponent,
        PermitReportComponent,
        NewSmarkPermitComponent
    ]
})
export class MaterialModule {}

@NgModule({
    imports: [
        CommonModule,
        BrowserAnimationsModule,
        FormsModule,
        // RouterModule.forRoot(routes, {
        //     useHash: true
        // }),
        HttpClientModule,
        MaterialModule,
        SidebarModule,
        NavbarModule,
        FooterModule,
        RouterModule,
        AppRoutingModule,
        CoreModule,
        ToastrModule.forRoot({
            timeOut: 10000,
            enableHtml: true,
            newestOnTop: false,
            maxOpened: 1,
            autoDismiss: false,
            positionClass: 'toast-top-right',
            preventDuplicates: true,
        }),
        ReactiveFormsModule,
        SharedModule,
        FormsModule,
        NgxPaginationModule,
        FontAwesomeModule,
    ],
    declarations: [
        AppComponent,
        AdminLayoutComponent,
        AuthLayoutComponent
    ],
    providers : [
        MatNativeDateModule,
        // {provide: APP_BASE_HREF, useValue: '/migration/'}
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }
