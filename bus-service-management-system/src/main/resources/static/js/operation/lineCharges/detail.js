// ======================================================
// 運賃区分（均一 / 三角）切り替え制御
//  - 初期表示制御
//  - 入力の有効 / 無効
//  - 必須バッジ表示
//  - 非選択側の値リセット
//  - paymentMethod / uniformTransferType の初期選択
// ======================================================
document.addEventListener("DOMContentLoaded", () => {

  const root = document.querySelector(".line-charge");
  if (!root) return;

  const radios = Array.from(
      document.querySelectorAll('input[name="lineChargeDivision"]')
  );

  // writeFlag=false（閲覧系）でのみ存在する想定の hidden（lineChargeDivision用）
  const hiddenDivision = document.querySelector(
      'input[type="hidden"][name="lineChargeDivision"]'
  );

  // --------------------------------------
  // radio のデフォルト補正（未選択のときだけ）
  // --------------------------------------
  function ensureDefaultRadio(name, defaultValue) {
    const checked = document.querySelector(`input[name="${name}"]:checked`);
    if (checked) return checked.value;

    const target =
        document.querySelector(`input[name="${name}"][value="${defaultValue}"]`) ||
        document.querySelector(`input[name="${name}"]`);

    if (target) {
      target.checked = true;
      return target.value;
    }
    return null;
  }

  // --------------------------------------
  // writeFlag=false（閲覧系）で hidden を併用してる場合の同期
  // ※ hidden の name が radio と同じ前提
  // --------------------------------------
  function syncHiddenIfExists(name, value) {
    const h = document.querySelector(`input[type="hidden"][name="${name}"]`);
    if (h) h.value = value;
  }

  // --------------------------------------
  // 現在の運賃区分を取得（radio優先 → hidden）
  // --------------------------------------
  function getDivision() {
    const checked = document.querySelector('input[name="lineChargeDivision"]:checked');
    if (checked) return checked.value;

    if (hiddenDivision && hiddenDivision.value) return hiddenDivision.value;

    return null;
  }

  // --------------------------------------
  // 初期値が未設定なら均一(1)に補正
  // --------------------------------------
  function ensureDefaultDivision() {
    const val = getDivision();
    if (val) return val;

    const defaultRadio =
        document.querySelector('input[name="lineChargeDivision"][value="1"]') ||
        document.querySelector('input[name="lineChargeDivision"]');

    if (defaultRadio) {
      defaultRadio.checked = true;
      if (hiddenDivision) hiddenDivision.value = defaultRadio.value;
      return defaultRadio.value;
    }
    return null;
  }

  // --------------------------------------
  // 入力の有効 / 無効切り替え
  // --------------------------------------
  function setEnabled(groupName, enabled) {
    document.querySelectorAll(`[data-group="${groupName}"]`).forEach(el => {
      el.disabled = !enabled;
    });
  }

  // --------------------------------------
  // 必須表示の切り替え
  // --------------------------------------
  function setRequiredVisible(groupName, showRequired) {
    document
        .querySelectorAll(`.req-badge[data-req-group="${groupName}"]`)
        .forEach(el => (el.style.display = showRequired ? "" : "none"));

    document
        .querySelectorAll(`.req-spacer[data-req-group="${groupName}"]`)
        .forEach(el => (el.style.display = showRequired ? "none" : "inline-block"));
  }

  // --------------------------------------
  // 状態反映の本体
  // --------------------------------------
  function applyAll() {
    const val = getDivision();
    if (!val) return;

    const isUniform  = val === "1";
    const isTriangle = val === "2";

    // CSS制御用
    root.classList.toggle("lcd-2", isTriangle);

    // 入力制御
    setEnabled("uniform",  isUniform);
    setEnabled("triangle", isTriangle);

    // 必須表示制御
    setRequiredVisible("uniform",  isUniform);
    setRequiredVisible("triangle", isTriangle);

    // ★均一を有効にした時、乗換可否が未選択なら 0 を入れる
    if (isUniform) {
      const ut = ensureDefaultRadio("uniformTransferType", "0");
      if (ut != null) syncHiddenIfExists("uniformTransferType", ut);
    }
  }

  // ======================================================
  // 初期表示
  // ======================================================

  // ★支払タイミング：未選択なら 1
  const pm = ensureDefaultRadio("paymentMethod", "0");
  if (pm != null) syncHiddenIfExists("paymentMethod", pm);

  // 運賃区分：未選択なら 均一(1)
  ensureDefaultDivision();

  // 反映
  applyAll();

  // ======================================================
  // 運賃区分変更時
  // ======================================================
  radios.forEach(r => {
    r.addEventListener("change", () => {
      if (hiddenDivision) hiddenDivision.value = r.value;
      applyAll();
    });
  });

});
