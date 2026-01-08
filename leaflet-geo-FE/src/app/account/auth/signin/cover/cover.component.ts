import { Component, OnInit } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { firstValueFrom, take } from 'rxjs';
import { AuthenticationService } from 'src/app/services/auth.service';
import { LanguageService } from 'src/app/services/language.service';
import { ToastService } from 'src/app/shared/services/toast-service';
import { Store } from '@ngrx/store';
import { setUser } from 'src/app/store/auth/auth.action';
import { selectCurrentUser } from 'src/app/store/auth/auth.selector';
import { ApiResponse, LoginResponse } from 'src/app/models/auth.models';

@Component({
  selector: 'app-cover',
  templateUrl: './cover.component.html',
  styleUrls: ['./cover.component.scss']
})

/**
 * Cover Component - Login Page
 */
export class CoverComponent implements OnInit {

  // Login Form
  loginForm!: UntypedFormGroup;
  submitted = false;
  fieldTextType!: boolean;
  error = '';
  returnUrl!: string;
  // set the current year
  year: number = new Date().getFullYear();
  // Carousel navigation arrow show
  showNavigationArrows: any;
  userData!: any;
  alertType: 'success' | 'danger' | null = null;
  alertMessage: string | null = null;

  constructor(
    private formBuilder: UntypedFormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    public toastService: ToastService,
    public translate: TranslateService,
    public languageService: LanguageService,
    private spinner: NgxSpinnerService,
    private store: Store
  ) {
    this.translate.setDefaultLang('en');
  }

  async ngOnInit(): Promise<void> {
    // Check if already logged in
    this.store.select(selectCurrentUser)
      .pipe(take(1))
      .subscribe(user => {
        this.userData = user;
        if (this.userData) {
          this.router.navigate(['/']);
        }
      });

    // Form Validation - changed employeeCode to username
    this.loginForm = this.formBuilder.group({
      username: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  /**
   * Form submit - Login
   */
  async onSubmit() {
  this.submitted = true;

  // 1. Validasi form
  if (!this.f['username'].value || !this.f['password'].value) {
    return;                 // kalau invalid, langsung keluar (spinner tidak dinyalakan)
  }

  this.spinner.show();      // 2. Tampilkan spinner

  try {
    // 3. Panggil API login (async)
    const payload = {
      username: this.f['username'].value,
      password: this.f['password'].value,
    };

    // const res = await this.authService.login(payload).toPromise();
    // 4. Tangani respon sukses (simpan token, redirect, dll)
    // this.router.navigate(['/dashboard']);
  } catch (err) {
    // 5. Tangani error (tampilkan pesan ke user)
    // this.errorMessage = 'Username atau password salah';
  } finally {
    // 6. Matikan spinner apapun hasilnya
    this.spinner.hide();
  }
}

}
