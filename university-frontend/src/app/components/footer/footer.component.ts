/*
import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css'
})
export class FooterComponent {

}
*/

// ========================================
// FOOTER COMPONENT
// ========================================

// src/app/components/footer/footer.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { LucideAngularModule, GraduationCap, Phone, Mail, MapPin, Facebook, Twitter, Instagram, Linkedin } from 'lucide-angular';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, RouterModule, LucideAngularModule],
  template: `
    <footer class="bg-gray-900 text-white py-12">
      <div class="max-w-7xl mx-auto px-4">
        <div class="grid md:grid-cols-5 gap-8 mb-8">
          <div class="md:col-span-2">
            <div class="flex items-center mb-4">
              <div class="bg-gradient-to-br from-blue-600 to-purple-600 p-2 rounded-lg">
                <lucide-icon [img]="GraduationCap" [size]="32" class="text-white"></lucide-icon>
              </div>
              <span class="ml-2 text-xl font-bold">Excellence University</span>
            </div>
            <p class="text-gray-400 mb-4">Transforming lives through education since 1950. Join our community of 25,000+ students from around the world.</p>
            <div class="flex gap-3">
              <lucide-icon [img]="Facebook" [size]="24" class="text-gray-400 hover:text-white cursor-pointer transition-colors"></lucide-icon>
              <lucide-icon [img]="Twitter" [size]="24" class="text-gray-400 hover:text-white cursor-pointer transition-colors"></lucide-icon>
              <lucide-icon [img]="Instagram" [size]="24" class="text-gray-400 hover:text-white cursor-pointer transition-colors"></lucide-icon>
              <lucide-icon [img]="Linkedin" [size]="24" class="text-gray-400 hover:text-white cursor-pointer transition-colors"></lucide-icon>
            </div>
          </div>

          <div>
            <h3 class="font-bold mb-4 text-lg">Academics</h3>
            <ul class="space-y-2 text-sm">
              <li><a routerLink="/programs" class="text-gray-400 hover:text-white transition-colors">Programs</a></li>
              <li><a routerLink="/admissions" class="text-gray-400 hover:text-white transition-colors">Admissions</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Financial Aid</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Academic Calendar</a></li>
            </ul>
          </div>

          <div>
            <h3 class="font-bold mb-4 text-lg">Campus Life</h3>
            <ul class="space-y-2 text-sm">
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Student Organizations</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Housing</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Dining</a></li>
              <li><a href="#" class="text-gray-400 hover:text-white transition-colors">Recreation</a></li>
            </ul>
          </div>

          <div>
            <h3 class="font-bold mb-4 text-lg">Contact</h3>
            <ul class="space-y-3 text-sm text-gray-400">
              <li class="flex items-start">
                <lucide-icon [img]="MapPin" [size]="16" class="mr-2 flex-shrink-0 mt-0.5"></lucide-icon>
                123 University Avenue<br/>Metro City, ST 12345
              </li>
              <li class="flex items-center">
                <lucide-icon [img]="Phone" [size]="16" class="mr-2"></lucide-icon>
                +1 (555) 123-4567
              </li>
              <li class="flex items-center">
                <lucide-icon [img]="Mail" [size]="16" class="mr-2"></lucide-icon>
                info"&#64;"excellence.edu
              </li>
            </ul>
          </div>
        </div>

        <div class="border-t border-gray-800 pt-8 flex flex-col md:flex-row justify-between items-center text-sm text-gray-400">
          <p>&copy; 2025 Excellence University. All rights reserved.</p>
          <div class="flex gap-6 mt-4 md:mt-0">
            <a href="#" class="hover:text-white transition-colors">Privacy Policy</a>
            <a href="#" class="hover:text-white transition-colors">Terms of Service</a>
            <a href="#" class="hover:text-white transition-colors">Accessibility</a>
          </div>
        </div>
      </div>
    </footer>
  `
})
export class FooterComponent {
  readonly GraduationCap = GraduationCap;
  readonly Phone = Phone;
  readonly Mail = Mail;
  readonly MapPin = MapPin;
  readonly Facebook = Facebook;
  readonly Twitter = Twitter;
  readonly Instagram = Instagram;
  readonly Linkedin = Linkedin;
}