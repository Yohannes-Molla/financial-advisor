# LedgerOne — Antigravity Build Instructions

You are building a **minimal, privacy-first financial assistant Android app** for users in Ethiopia.

The app helps users understand their money by reading transaction confirmations (SMS/notifications/receipts), organizing them, and presenting them in a simple and intuitive way.

---

# 🎯 CORE PRODUCT PRINCIPLES

1. **Clarity over features**
   - Show only what helps the user understand or act.
   - If there is no data → DO NOT show the section.

2. **Minimalism**
   - No clutter.
   - No decorative UI.
   - No financial jargon.

3. **Trust**
   - Everything is processed on-device.
   - Be transparent about data usage.

4. **Speed**
   - App must feel fast and lightweight.

---

# 🧭 APP STRUCTURE (3 MAIN TABS)

Bottom navigation has exactly 3 tabs:

1. Money
2. Plan
3. Verify

No additional tabs in MVP.

---

# 1️⃣ MONEY TAB (MAIN DASHBOARD)

## Purpose
Show current money situation and recent activity.

## UI STRUCTURE (Top to Bottom)

### A. Total Balance
Show ONLY if at least 1 account exists.

Display:
- Large number (ETB)
- Subtitle: "Across X accounts"

If no data:
> "No transactions yet — your money activity will appear here"

---

### B. Account Cards
Show ONLY if more than 1 account is detected.

Each card shows:
- Bank name (CBE, Telebirr, Awash, etc.)
- Last known balance
- Last activity date

If only 1 account → DO NOT show cards.

---

### C. Money Flow Timeline (CORE FEATURE)
Always show when transactions exist.

Each row:
- + / – amount
- source (bank/service)
- short description
- timestamp
- verification badge (if verified)

---

### D. Simple Insight (Optional)
Show only when meaningful.

Examples:
- "You spent more than you received this week"
- "Your Telebirr balance dropped quickly in the last 3 days"

If no meaningful insight → DO NOT render.

---

### E. Charts (Strict Conditions)

Only show charts if:
- user has ≥10 transactions
- and ≥7 days of activity

Allowed charts:
1. Balance over time (line chart)
2. Income vs Expense (bar chart)

Otherwise → hide chart section completely.

---

# 2️⃣ PLAN TAB (FINANCIAL PLANNER)

## Purpose
Prevent the user from running out of money.

## UI STRUCTURE

### A. Upcoming Payments
Show only if user created reminders.

Each item:
- title
- amount
- due date

If empty:
> "No planned payments yet"

---

### B. Add Reminder Button
Single primary button:
➕ Add Reminder

Fields:
- title
- amount
- date
- optional repeat

---

### C. Future Balance Hint
Show only if reminders exist.

Display a single sentence:
> "After upcoming payments, you’ll have about X ETB left"

No charts required in MVP.

---

# 3️⃣ VERIFY TAB (TRANSACTION PROOF)

## Purpose
Allow users to confirm payments are real.

## UI STRUCTURE

### A. Quick Verify Options
Provide 3 methods:
- Paste receipt link
- Enter transaction ID
- Upload screenshot

---

### B. Verification Result Card
After verification show:

- amount
- sender
- receiver
- date
- status:
  - Verified
  - Not Found

---

### C. Verification History
Show only if user has previous verifications.

List items:
- verified ✔
- failed ❌

No charts in MVP.

---

# 🔐 PERMISSIONS STRATEGY

Permissions must be requested progressively and with clear explanation.

## Permissions

### 1. Notification Access
Purpose: detect incoming transaction messages
When: during onboarding
Message to user:
> "LedgerOne reads transaction alerts to track your money automatically."

---

### 2. SMS Access (READ_SMS)
Purpose: extract full transaction details
When: only after user sees value of app

Explain clearly:
> "We only read bank transaction messages. Your personal messages are never used."

---

### 3. Internet Access
Purpose: fetch and verify receipt links
When: only when user verifies transaction

---

# 🔐 PRIVACY RULES

- All parsing must happen on-device
- Do not upload raw SMS data
- Do not store unnecessary personal content
- Allow user to disable SMS reading anytime
- Provide local-only mode

---

# 🎨 DESIGN GUIDELINES

## Visual Style
- Clean
- White background
- Minimal color usage

## Colors
- Green = income
- Red = expense
- Blue = primary UI

## Typography
Hierarchy:
1. Large numbers (balance)
2. Medium (transaction amount)
3. Small (metadata)

Use large readable spacing.

---

# 🚫 WHAT NOT TO BUILD (MVP)

Do NOT include:

- budgeting categories
- pie charts
- advanced filters
- financial jargon
- AI predictions
- multi-screen complex flows

---

# 🧪 MVP FEATURE SET

Must include:

- SMS/notification transaction detection
- transaction parsing
- unified transaction timeline
- account grouping
- total balance calculation
- receipt link verification
- reminder system (simple)

---

# 🌍 LOCAL CONTEXT (ETHIOPIA)

The app must support transaction formats from:

- Commercial Bank of Ethiopia (CBE)
- Telebirr
- Bank of Abyssinia
- Awash Bank
- Dashen Bank

Currency: ETB

Handle formats like:
"debited with ETB 502.59"
"credited ETB 1,000"

---

# 🧠 UX LANGUAGE

Use simple English.

Avoid financial jargon.

Preferred wording:
- Money In
- Money Out
- Planned
- Verified

---

# 🧩 ENGINEERING RULES

- Use modular architecture
- Ensure fast load time
- Avoid heavy UI rendering
- Lazy load charts
- Cache transaction data locally

---

# 🏁 SUCCESS CRITERIA

The app is successful if:

- User opens and immediately sees their recent transactions
- User understands where their money went in <5 seconds
- User can verify a payment easily
- User does not feel overwhelmed

---

# END OF INSTRUCTIONS
